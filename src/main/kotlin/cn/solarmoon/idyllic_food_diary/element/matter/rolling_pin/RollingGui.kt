package cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin

import cn.solarmoon.idyllic_food_diary.feature.optinal_recipe.OptionalRecipeGui
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class RollingGui: OptionalRecipeGui() {
    override val matchItems: List<Item>
        get() = listOf(IFDItems.ROLLING_PIN.get())

    override fun getItemsShown(
        input: Block,
        level: Level
    ): List<ItemStack> {
        val recipeMatches = RollingPinItem.findRecipes(input, level)
        return recipeMatches.map {
            if (it.value.output == Blocks.AIR) return@map it.value.results[0]
            else return@map ItemStack(it.value.output)
        }
    }
}