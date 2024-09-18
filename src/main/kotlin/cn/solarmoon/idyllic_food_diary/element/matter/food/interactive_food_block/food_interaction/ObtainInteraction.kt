package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodBlockEntity
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.idyllic_food_diary.feature.util.MessageUtil
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

/**
 * 从目标食物方块的每个阶段获取指定物品
 * @param container 手持容器，会识别是否匹配
 * @param containerTypeName 手持容器的自定义种类名，比如刀类，会显示在交互失败的提示中
 * @param dropForm 掉落形式，可选直接掉落到地上或给到玩家物品栏中
 * @param obtainingMethod 获取形式，可选是分割还是盛取，会影响交互失败的提示以及是否损耗手中物品
 * @param sound 交互时发出的声音
 * @param destroyParticle 是否产生破坏方块的粒子
 */
data class ObtainInteraction(
    val container: Ingredient,
    val containerTypeName: Component,
    val result: ItemStack,
    val dropForm: DropForm,
    val obtainingMethod: ObtainingMethod,
    val sound: SoundEvent = SoundEvents.ARMOR_EQUIP_LEATHER.value(),
    val destroyParticle: Boolean = false
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
        if (container.test(heldItem)) {
            // 首先替换方块到下一阶段，如果目标阶段为0则放置留存方块
            fb.consumeInteraction(destroyParticle)
            // 根据method决定物品是消耗还是消耗耐久
            if (!heldItem.isEmpty) {
                when (obtainingMethod) {
                    ObtainingMethod.SERVE -> heldItem.shrink(1) // 先消耗物品防止挤占槽位
                    ObtainingMethod.SPLIT -> heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
                }
            }
            // 根据dropForm决定生成掉落物还是直接进入玩家背包
            when (dropForm) {
                DropForm.DROP_TO_GROUND -> DropUtil.summonDrop(result, level, pos)
                DropForm.DROP_TO_INVENTORY -> DropUtil.addItemToInventory(player, result)
            }
            level.playSound(null, pos, sound, SoundSource.PLAYERS)
            return true
        } else {
            // 基本的条件不满足就发送消息提示
            player.displayClientMessage(obtainingMethod.getRequiredMessage(containerTypeName), true)
            return false
        }
    }

    enum class DropForm {
        DROP_TO_GROUND, DROP_TO_INVENTORY
    }

    enum class ObtainingMethod {
        SERVE, SPLIT;
        fun getRequiredMessage(containerTypeName: Component): Component {
            return if (this == SERVE) {
                IdyllicFoodDiary.TRANSLATOR.set("message", "obtainable_food.container_required", containerTypeName)
            } else {
                IdyllicFoodDiary.TRANSLATOR.set("message", "obtainable_food.tool_required", containerTypeName)
            }
        }
    }

}
