package cn.solarmoon.idyllic_food_diary.element.matter.cleaver

import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.OptionalRecipeGui
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class CleaverGui: OptionalRecipeGui() {
    override val matchItems: List<Item>
        get() = listOf(IFDItems.CLEAVER.get())

    override fun getItemsShown(
        input: Block,
        level: Level
    ): List<ItemStack> {
        return CleaverItem.findRecipes(ItemStack(input), level).map { it.value.results[0] }
    }
}