package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack.SkewerRackBlockEntity.Companion.ANIM_ROTATION
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockentity.HandyEntityBlock
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.IWaterLoggedState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import kotlin.collections.set
import kotlin.math.min

class SkewerRackBlock: HandyEntityBlock(Properties.of().sound(SoundType.WOOD).strength(2f)), IHorizontalFacingState, IWaterLoggedState {

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val rack = level.getBlockEntity(pos) as SkewerRackBlockEntity
        val structure = rack.structure ?: return InteractionResult.PASS

        if (!player.mainHandItem.isEmpty) return InteractionResult.FAIL

        if (!level.isClientSide && pos in listOf(structure.leftTerminal.blockPos, structure.rightTerminal.blockPos)) {
            structure.roll()
            return InteractionResult.SUCCESS
        }

        return InteractionResult.FAIL
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val rack = level.getBlockEntity(pos) as SkewerRackBlockEntity
        val direction = state.getValue(IHorizontalFacingState.FACING)
        val side = hitResult.direction
        if (side !in listOf(direction.clockWise, direction.counterClockWise) && ItemStackHandlerHelper.storage(rack.inventory, player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        val rack = blockEntity as SkewerRackBlockEntity
        if (rack.isConnectedWithFence) rack.preventItemIn()

        // 旋转角
        val anim = rack.getData(SparkAttachments.ANIMTICKER)
        val timerRot = anim.timers[ANIM_ROTATION]!!
        if (timerRot.isTiming) {
            var v = (timerRot.maxTime - timerRot.time) / 20f
            v = min(v, 1f) * 3 // 配合上一行看，最大tick是30，留10tick，期间不减速作为启动缓冲
            anim.fixedValues.put("rot", anim.fixedValues.getOrDefault("rot", 0f) + v)
            anim.fixedValues.put("velocity", v)
        }

        rack.syncRot()

    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        level.getBlockEntity(pos)?.let { if (it is SkewerRackBlockEntity) it.syncRot() }
    }

    companion object {
        /**
         * 烤架本体的碰撞箱
         */
        @JvmStatic
        fun getOriginShape(direction: Direction) = VoxelShapeUtil.rotateShape(direction, box(0.0, 7.0, 7.0, 16.0, 9.0, 9.0))
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val origin = getOriginShape(state.getValue(IHorizontalFacingState.FACING))
        var add = Shapes.empty()
        val rack = level.getBlockEntity(pos)
        if (rack is SkewerRackBlockEntity) {
            rack.getConnectedValidFence()?.let {
                add = it.block.defaultBlockState().getShape(level, pos)
            }
        }
        return Shapes.or(origin, add)
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.ENTITYBLOCK_ANIMATED
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.SKEWER_RACK.get()
    }

}