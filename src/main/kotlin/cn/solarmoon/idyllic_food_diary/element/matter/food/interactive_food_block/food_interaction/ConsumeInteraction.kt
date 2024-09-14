package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.PlayerUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

/**
 * 消耗型交互，每次交互将食用指定次数然后消耗并恢复饱食度数据
 * @param preEatCount 每次真正吃掉所需要右键的次数
 * @param foodProperty 吃掉的食物属性
 * @param eatSound 进食音效
 */
data class ConsumeInteraction(
    val preEatCount: Int,
    val foodProperty: FoodProperties,
    val eatSound: SoundEvent = SoundEvents.GENERIC_EAT
): IInteraction {
    override fun doInteraction(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): Boolean {
        val fb = level.getBlockEntity(pos) as FoodBlockEntity
        if (player.canEat(foodProperty.canAlwaysEat)) {
            val stage = state.getValue(FoodEntityBlock.INTERACTION);
            if (stage > 0) {
                val counting = player.getData(SparkAttachments.COUNTING_DEVICE)
                counting.setCount(counting.count + 1, pos)
                //吃的声音
                level.playSound(null, pos, eatSound, SoundSource.PLAYERS)
                //吃的粒子效果
                player.spawnItemParticles(state.getCloneItemStack(hitResult, level, pos, player), 5)
                val targetState = state.setValue(FoodEntityBlock.INTERACTION, stage - 1)
                if (counting.count >= preEatCount) {
                    level.setBlock(pos, targetState, 3)
                    PlayerUtil.eat(player, foodProperty)
                    counting.setCount(0)
                    if (targetState.getValue(FoodEntityBlock.INTERACTION) == 0) {
                        BlockUtil.replaceBlockWithAllState(state, fb.containerBlockState, level, pos);
                    }
                }
                return true;
            }
        }
        return false;
    }
}
