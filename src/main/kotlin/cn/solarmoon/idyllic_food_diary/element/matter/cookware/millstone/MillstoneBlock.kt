package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.feature.util.ParticleUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.block.caller.IBlockUseCaller
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.util.DropUtil
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidUtil
import kotlin.math.min

class MillstoneBlock(properties: Properties = Properties.of()
    .sound(SoundType.STONE)
    .strength(2.5f)
    .noOcclusion()): CookwareBlock(properties), IBlockUseCaller {

    override fun useItemOnThis(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        // 蹲下才能手抽物品
        val mill = level.getBlockEntity(pos) as MillstoneBlockEntity
        if (!player.isCrouching) {
            if (!level.isClientSide && handle(mill)) return InteractionResult.SUCCESS
        } else if (handleInvAct(mill, player)) return InteractionResult.sidedSuccess(level.isClientSide)
        return super.useWithoutItem(state, level, pos, player, hitResult)
    }

    companion object {
        /**
         * 开始摇动石磨
         */
        @JvmStatic
        fun handle(mill: MillstoneBlockEntity): Boolean {
            if (!mill.grind.isRecipeSmooth()) return false
            mill.getData(SparkAttachments.ANIMTICKER).timers["rotation"]!!.start()
            mill.setChanged()
            return true
        }

        /**
         * 空手从石磨中抽取物品，每次抽一组
         */
        private fun handleInvAct(mill: MillstoneBlockEntity, player: Player): Boolean {
            for (i in mill.inventory.slots - 1 downTo 0) {
                if (!mill.inventory.realExtract(i, 64, true).isEmpty) {
                    DropUtil.addItemToInventory(player, mill.inventory.realExtract(i, 64, false))
                    return true
                }
            }
            return false
        }
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        val mill = blockEntity as MillstoneBlockEntity
        mill.grind.tryWork()

        // 空气漏斗
        level.getEntities(null, AABB(pos.above()).setMaxY(pos.above().y + 1 / 16.0)).forEach { entity ->
            if (entity is ItemEntity) {
                if (!ItemStack.matches(ItemStackHandlerHelper.insertItem(mill.inventory, entity.item), entity.item)) {
                    entity.remove(Entity.RemovalReason.DISCARDED)
                }
            }
        }

        // 传递输出液体
        mill.grind.transferOfFluid();

        //————————————————————————————————————————动画————————————————————————————————————————————————//
        val anim = mill.getData(SparkAttachments.ANIMTICKER)
        val timerRot = anim.timers[MillstoneBlockEntity.ANIM_ROTATION]!!
        val timerFlow = anim.timers[MillstoneBlockEntity.ANIM_FLOW]!!

        // 声音和粒子
        if (timerRot.isTiming) {
            val ingredient = mill.inventory.getStackInSlot(0)
            var pt = anim.fixedValues.getOrDefault("particle", 0f)
            if (!ingredient.isEmpty && pt % 2 == 0f) {
                ParticleUtil.rolling(pos, level, ingredient, 3/16.0, 1)
                val scale = anim.fixedValues.getOrDefault("velocity", 0f)
                level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.5F * scale, 0.5F);
            }
            pt++
            if (pt > 5) {
                pt = 0f
            }
            anim.fixedValues.put("particle", pt)
        }

        // 旋转角
        if (timerRot.isTiming) {
            var v = (timerRot.maxTime - timerRot.time) / 20f
            v = min(v, 1f) * 3 // 最大tick是30，留10tick，期间不减速作为启动缓冲
            anim.fixedValues.put("rot", anim.fixedValues.getOrDefault("rot", 0f) + v)
            anim.fixedValues.put("velocity", v)
        }

        // 液体类水管下流动画
        val flowMaxTick = 10f
        var tick = anim.fixedValues.getOrDefault("flow", 0f)
        if (mill.grind.flowing && tick < flowMaxTick) {
            tick++
        } else if (!mill.grind.flowing && tick > 0) {
            tick--
        }
        anim.fixedValues.put("flow", tick)
        anim.fixedValues.put("flowMax", flowMaxTick)
    }

    override fun getShapeThis(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val direction = state.getValue(IHorizontalFacingState.FACING)
        val body = box(3.0, 1.0, 3.0, 13.0, 9.0, 13.0)
        val trench = Shapes.joinUnoptimized(box(1.0, 1.0, 1.0, 15.0, 3.0, 15.0), box(3.0, 1.0, 3.0, 13.0, 3.0, 13.0), BooleanOp.ONLY_FIRST)
        val bottom = box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);
        val rB = Shapes.joinUnoptimized(bottom, trench, BooleanOp.ONLY_FIRST);
        val r = Shapes.or(rB, box(6.0, 0.0, -3.0, 10.0, 3.0, 1.0));
        val o = Shapes.joinUnoptimized(r, box(7.0, 1.0, -3.0, 9.0, 3.0, 1.0), BooleanOp.ONLY_FIRST);
        return VoxelShapeUtil.rotateShape(direction, Shapes.or(o, body));
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.MILLSTONE.get()
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.ENTITYBLOCK_ANIMATED
    }

    override fun getUseResult(
        state: BlockState,
        pos: BlockPos,
        level: Level,
        player: Player,
        heldItem: ItemStack,
        hitResult: HitResult,
        hand: InteractionHand
    ): TriState {
        // 蹲下先取出而不是转盘
        if (player.isCrouching && heldItem.isEmpty) return TriState.TRUE

        return TriState.DEFAULT
    }

    override fun dropSync(): Boolean {
        return false
    }

    override fun canGet(): Boolean {
        return false
    }

}