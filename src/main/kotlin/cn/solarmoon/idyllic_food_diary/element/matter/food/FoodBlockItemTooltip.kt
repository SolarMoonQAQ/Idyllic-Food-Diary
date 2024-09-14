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
import org.joml.Matrix4f
import java.awt.Color
import kotlin.math.max

class FoodBlockItemTooltip(component: Component): CustomTooltip<FoodBlockItemTooltip.Component>(component) {

    /**
     * 每行的固定高度
     */
    val hPre = 12

    /**
     * 让物品高度居中所需的固定偏移量
     */
    val itemOffset = 4

    val containerText
        get() = IdyllicFoodDiary.TRANSLATOR.set("tooltip", "container", component.container.hoverName)
    val shiftOnText
        get() = TooltipOperator.SHIFT_ON
    val shiftOffText
        get() = TooltipOperator.SHIFT_OFF

    override fun getHeight(): Int {
        if (shouldRender()) {
            return if (!Screen.hasShiftDown()) hPre
            else hPre * 2
        }
        return 0
    }

    override fun getWidth(font: Font): Int {
        if (shouldRender()) {
            return if (!Screen.hasShiftDown()) font.width(shiftOffText)
            else maxOf(font.width(shiftOffText), font.width(shiftOnText), font.width(containerText) + 16)
        }
        return 0
    }

    fun shouldRender(): Boolean {
        return component.stack.item is FoodBlockItem && !component.container.isEmpty
    }

    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        if (!shouldRender()) return
        if (!Screen.hasShiftDown()) {
            guiGraphics.drawString(font, shiftOffText, x, y, Color.WHITE.rgb)
        } else {
            guiGraphics.drawString(font, shiftOnText, x, y, Color.WHITE.rgb)
            guiGraphics.drawString(font, containerText, x, y + hPre, Color.WHITE.rgb)
            guiGraphics.renderItem(component.container, x + font.width(containerText), y + hPre - itemOffset)
        }
    }

    data class Component(val stack: ItemStack): TooltipComponent {
        val container
            get() = stack.getOrDefault(IFDDataComponents.FOOD_CONTAINER, FoodContainer.EMPTY).stack
    }

}