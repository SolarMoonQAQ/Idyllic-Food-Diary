package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult

abstract class LongFoodEntityBlock(properties: Properties): FoodEntityBlock(properties), IBedPartState {

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val realPos = IBedPartState.getFootPos(state, pos)
        return super.useItemOn(stack, state, level, realPos, player, hand, hitResult)
    }

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player
    ): ItemStack {
        val realPos = IBedPartState.getFootPos(state, pos)
        return super.getCloneItemStack(state, target, level, realPos, player)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        val headPos = pos.relative(IBedPartState.getNeighbourDirection(state.getValue(IBedPartState.PART), state.getValue(IHorizontalFacingState.FACING)))
        level.setBlock(headPos, level.getBlockState(headPos).setValue(INTERACTION, level.getBlockState(pos).getValue(INTERACTION)), 3)
    }

}