package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable
import org.checkerframework.checker.units.qual.s

/**
 * 只是个通用交互处理器，不保存任何自身数据，实际上就是个实用类
 */
class SteamerItemHandler(private val invs: MutableList<SteamerInventory>): IItemHandlerModifiable {

    val sizePreLayer = 4.takeIf { invs.isEmpty() } ?: invs[0].slots

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        val layer = slot /sizePreLayer
        val thisSlot = slot - layer * sizePreLayer
        invs[layer].setStackInSlot(thisSlot, stack)
    }

    override fun getSlots(): Int {
        return invs.sumOf { it.slots }
    }

    override fun getStackInSlot(slot: Int): ItemStack {
        val layer = slot / sizePreLayer
        val thisSlot = slot - layer * sizePreLayer
        return invs[layer].getStackInSlot(thisSlot)
    }

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        simulate: Boolean
    ): ItemStack {
        val layer = slot /sizePreLayer
        val thisSlot = slot - layer * sizePreLayer
        return invs[layer].insertItem(thisSlot, stack, simulate)
    }

    override fun extractItem(
        slot: Int,
        amount: Int,
        simulate: Boolean
    ): ItemStack {
        val layer = slot /sizePreLayer
        val thisSlot = slot - layer * sizePreLayer
        return invs[layer].extractItem(thisSlot, amount, simulate)
    }

    // 下面两个没用，改[SteamerInventory]里的

    override fun getSlotLimit(slot: Int): Int {
        return 1
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return true
    }

}