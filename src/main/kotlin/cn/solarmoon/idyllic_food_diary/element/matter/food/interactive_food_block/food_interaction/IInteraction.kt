package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction

import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

interface IInteraction {

    fun doInteraction(stack: ItemStack,
                      state: BlockState,
                      level: Level,
                      pos: BlockPos,
                      player: Player,
                      hand: InteractionHand,
                      hitResult: BlockHitResult): Boolean

}