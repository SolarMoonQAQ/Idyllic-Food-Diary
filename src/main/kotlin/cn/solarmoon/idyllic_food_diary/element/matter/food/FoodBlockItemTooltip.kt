package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.tooltip.CustomTooltip
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import org.joml.Matrix4f
import java.awt.Color
import kotlin.math.max

class FoodBlockItemTooltip(component: Component): CustomTooltip<FoodBlockItemTooltip.Component>(component) {

    /**
     * 每行的固定高度
     */
    val hPre = 12

    /**
     * 当前行数，如果要加一行内容需要将此值+1然后渲染时带上下面的高度差y0即可
     */
    var row = 0

    /**
     * 当前行数对应高度
     */
    val yOffset
        get() = row * hPre

    /**
     * 让物品高度居中所需的固定偏移量
     */
    val itemOffset = 4

    val tierText
        get() = component.tier.displayName
    val containerText
        get() = IdyllicFoodDiary.TRANSLATOR.set("tooltip", "food.container", component.container.hoverName)
    val interactionText
        get() = IdyllicFoodDiary.TRANSLATOR.set("tooltip", "food.interaction", component.stage)
    val shiftOnText
        get() = TooltipOperator.SHIFT_ON
    val shiftOffText
        get() = TooltipOperator.SHIFT_OFF

    override fun getHeight(): Int {
        var row = 2
        if (shouldRender()) {
            return if (!Screen.hasShiftDown()) hPre * 2
            else {
                if (!component.container.isEmpty) row++
                if (component.stage > 0) row++
                return hPre * row
            }
        }
        return 0
    }

    override fun getWidth(font: Font): Int {
        if (shouldRender()) {
            return if (!Screen.hasShiftDown()) font.width(shiftOffText)
            else maxOf(font.width(shiftOffText), font.width(shiftOnText), font.width(containerText) + 16, font.width(interactionText))
        }
        return 0
    }

    fun shouldRender(): Boolean {
        return component.stack.item is FoodBlockItem
    }

    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        if (!shouldRender()) return
        guiGraphics.drawString(font, tierText, x, y + yOffset, Color.WHITE.rgb)
        row++
        if (!Screen.hasShiftDown()) {
            guiGraphics.drawString(font, shiftOffText, x, y + yOffset, Color.WHITE.rgb)
        } else {
            guiGraphics.drawString(font, shiftOnText, x, y + yOffset, Color.WHITE.rgb)

            if (!component.container.isEmpty) {
                row++
                guiGraphics.drawString(font, containerText, x, y + yOffset, Color.WHITE.rgb)
                guiGraphics.renderItem(component.container, x + font.width(containerText), y + yOffset - itemOffset)
            }

            if (component.stage > 0) {
                row++
                guiGraphics.drawString(font, interactionText, x, y + yOffset, Color.WHITE.rgb)
            }
        }
    }

    data class Component(val stack: ItemStack): TooltipComponent {
        val container
            get() = stack.getOrDefault(IFDDataComponents.FOOD_CONTAINER, FoodContainer.EMPTY).stack
        val stage: Int
            get() {
                val block = Block.byItem(stack.item)
                val maxStage = if (block is FoodEntityBlock) block.maxInteraction else 0
                return stack.getOrDefault(IFDDataComponents.INTERACTION, maxStage)
            }
        val tier
            get() = (stack.item as FoodBlockItem).tier
    }

}