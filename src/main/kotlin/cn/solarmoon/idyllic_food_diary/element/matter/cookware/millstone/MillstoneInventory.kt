package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.spark_core.api.cap.item.TileInventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import test.be

class MillstoneInventory(val millstone: MillstoneBlockEntity, size: Int): TileInventory(millstone, size) {

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (slot == 0) return ItemStack.EMPTY
        return super.extractItem(slot, amount, simulate)
    }

    /**
     * 阻止一般情况下插入输出槽<br/>
     * 用下面的real方法插入
     */
    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (slot != 0) return stack
        return super.insertItem(slot, stack, simulate)
    }

    fun realInsert(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        return super.insertItem(slot, stack, simulate)
    }

    fun realExtract(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        return super.extractItem(slot, amount, simulate);
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        if (slot == 0) return millstone.grind.findRecipe(stack);
        return super.isItemValid(slot, stack)
    }

}