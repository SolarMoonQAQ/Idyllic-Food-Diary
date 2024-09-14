package cn.solarmoon.idyllic_food_diary.element.matter.food_container

import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileItemContainerHelper
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import test.b

/**
 * 当内部没有其它物品内容时，使用贴图，反之使用方块模型
 */
class FoodContainerItem(block: Block, properties: Properties = Properties()): BlockItem(block, properties) {

    companion object {
        /**
         * @return false使用方块模型，true使用简单物品贴图
         */
        @JvmStatic
        fun isPureTexture(container: ItemStack, level: Level): Boolean {
            val inv = TileItemContainerHelper.getInventory(container, level.registryAccess()) ?: return true
            return ItemStackHandlerHelper.getStacks(inv).isEmpty()
        }
    }

    override fun getDefaultMaxStackSize(): Int {
        return 16
    }

}