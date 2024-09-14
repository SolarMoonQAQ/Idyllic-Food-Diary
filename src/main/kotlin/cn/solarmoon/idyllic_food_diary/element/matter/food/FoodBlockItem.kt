package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

/**
 * 食用后返还容器，制作返还容器
 */
class FoodBlockItem(block: Block, properties: Properties = Properties()): BlockItem(block, properties) {

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        livingEntity.eat(level, stack);
        if (livingEntity is Player && !livingEntity.isCreative) {
            val container = stack.getOrDefault(IFDDataComponents.FOOD_CONTAINER, FoodContainer.EMPTY).stack.takeIf { !it.isEmpty } ?: return stack
            DropUtil.addItemToInventory(livingEntity, container)
        }
        return stack
    }

    override fun hasCraftingRemainingItem(stack: ItemStack): Boolean {
        return true
    }

    override fun getCraftingRemainingItem(itemStack: ItemStack): ItemStack {
        return itemStack.getOrDefault(IFDDataComponents.FOOD_CONTAINER, FoodContainer.EMPTY).stack
    }

    override fun getDefaultMaxStackSize(): Int {
        return 16
    }

}