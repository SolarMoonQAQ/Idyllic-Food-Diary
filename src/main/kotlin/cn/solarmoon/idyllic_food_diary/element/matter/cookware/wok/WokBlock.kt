package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.FluidUtil

class WokBlock(properties: Properties = Properties.of()
    .sound(SoundType.LANTERN)
    .strength(2f, 6.0F)
    .noOcclusion()): CookwareBlock(properties), IBuiltInStove {

    override fun useItemOnThis(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val pan = level.getBlockEntity(pos) as WokBlockEntity

        if (pan.fry.tryGiveResult(player, hand, true)) {
            return ItemInteractionResult.SUCCESS
        }

        // 上方是空气才能炒菜（否则食材嵌到方块里有点怪）
        if (player.mainHandItem.`is`(IFDItems.SPATULA.get()) && level.getBlockState(pos.above()).isAir && pan.fry.doStirFry()) {
            return ItemInteractionResult.SUCCESS
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!pan.fry.hasResult() && !pan.fry.canStirFry) {
            //能够存取液体
            if (FluidUtil.interactWithFluidHandler(player, hand, pan.fluidTank)) {
                return ItemInteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand == InteractionHand.MAIN_HAND && !player.mainHandItem.`is`(IFDItems.SPATULA.get())
                && ItemStackHandlerHelper.storage(pan.inventory, player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, pan: BlockEntity) {
        super.tick(level, pos, state, pan)
        if (pan is WokBlockEntity) {
            pan.fry.tryWork();
            //pan.tryApplyThermochanger();

            // 炒菜音效
            if (pan.fry.isWorking()) {
                if (pan.soundTick == 0 || pan.soundTick > 90) {
                    level.playSound(null, pos, IFDSounds.STIR_FRY.get(), SoundSource.BLOCKS, 1f, 1f)
                    pan.soundTick = 1
                }
                pan.soundTick++
            } else {
                pan.soundTick = 0
            }
        }
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        super.animateTick(state, level, pos, random)
        val pan = level.getBlockEntity(pos)
        // 炒菜时的锅气
        if (pan is WokBlockEntity) {
            if (pan.fry.isWorking()) {
                val rInRange = 2/16f + random.nextDouble() * 12/16 // 保证粒子起始点在锅内
                val vi = (random.nextDouble() - 0.5) / 5
                level.addParticle(ParticleTypes.SMOKE, pos.x + rInRange, pos.y + 1 / 16f + getYOffset(state), pos.z + rInRange, vi, 0.1, vi)
            }
        }
    }

    override fun getShapeThis(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val body = Shapes.joinUnoptimized(box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0), box(3.0, 1.0, 3.0, 13.0, 4.0, 13.0), BooleanOp.ONLY_FIRST)
        val handle = Shapes.or(box(0.0, 2.0, 5.0, 2.0, 4.0, 11.0), box(14.0, 2.0, 5.0, 16.0, 4.0, 11.0))
        return VoxelShapeUtil.rotateShape(state.getValue(IHorizontalFacingState.FACING), Shapes.or(body, handle))
    }

    override val yOffset: Double
        get() = 12.0 / 16.0

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.WOK.get()
    }

}