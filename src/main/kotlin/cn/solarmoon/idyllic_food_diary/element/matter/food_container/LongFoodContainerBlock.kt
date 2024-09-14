package cn.solarmoon.idyllic_food_diary.element.matter.food_container

import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.phys.BlockHitResult

abstract class LongFoodContainerBlock(soundType: SoundType): FoodContainerBlock(soundType), IBedPartState {

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val posReal = IBedPartState.getFootPos(state, pos)
        return super.useWithoutItem(state, level, posReal, player, hitResult)
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
        val posReal = IBedPartState.getFootPos(state, pos)
        return super.useItemOn(stack, state, level, posReal, player, hand, hitResult)
    }

}