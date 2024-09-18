package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

class SteamerInventory(size: Int): ItemStackHandler(size) {

    private var steamer: SteamerBlockEntity? = null

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        val level = steamer?.level ?: return false
        val recipes = level.recipeManager.getAllRecipesFor(IFDRecipes.STEAMING.type.get()).map { it.value }
        return recipes.any { it.input.test(stack) }
    }

    override fun getSlotLimit(slot: Int): Int {
        return 1
    }

    fun applyTile(steamer: SteamerBlockEntity) = apply {
        this.steamer = steamer
    }

    override fun onContentsChanged(slot: Int) {
        super.onContentsChanged(slot)
        steamer?.invalidateCapabilities()
        steamer?.setChanged()
    }

}