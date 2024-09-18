package cn.solarmoon.idyllic_food_diary.feature.util

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * 用于展示Ingredient的标识符，比如需要用盘类物品盛出一种食物，但是Ingredient并没有自带的标识字符串，此时便需要统一的标识符来表示“盘子”这一大类
 */
object MessageUtil {

    @JvmStatic
    val EMPTY_HAND = identifier("empty")
    @JvmStatic
    val PLATE = identifier("plate")

    @JvmStatic
    fun identifier(id: String): Component {
        return IdyllicFoodDiary.TRANSLATOR.set("identifier", id)
    }

    @JvmStatic
    fun identifier(item: ItemStack): Component {
        return item.hoverName
    }

}