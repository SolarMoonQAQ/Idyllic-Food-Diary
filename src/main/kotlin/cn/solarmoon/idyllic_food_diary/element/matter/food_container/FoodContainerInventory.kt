package cn.solarmoon.idyllic_food_diary.element.matter.food_container

import cn.solarmoon.spark_core.api.cap.item.TileInventory
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

class FoodContainerInventory(be: BlockEntity, size: Int, slotLimit: Int = 1): TileInventory(be, size, slotLimit) {

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return stack.get(DataComponents.FOOD) != null
    }

}