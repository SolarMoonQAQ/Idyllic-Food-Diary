package cn.solarmoon.idyllic_food_diary.compat.farmersdelight

import cn.solarmoon.idyllic_food_diary.element.recipe.StewRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import vectorwing.farmersdelight.common.registry.ModRecipeTypes
import vectorwing.farmersdelight.common.tag.ModTags
import kotlin.math.roundToInt

object FarmersUtil {

    @JvmStatic
    fun isHeatSource(state: BlockState): Boolean {
        return FarmersDelightCompat.isLoaded() && state.`is`(ModTags.HEAT_SOURCES)
    }

    /**
     * 将农夫乐事的所有烹饪锅配方加入炖煮配方
     */
    @JvmStatic
    fun addAllRecipeToIFDCookingPotRecipe(combination: MutableList<StewRecipe>, level: Level) {
        if (FarmersDelightCompat.isLoaded()) {
            val fdRecipes = level.recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get())
            for (fdRecipe in fdRecipes) {
                val recipe = fdRecipe.value
                combination.add(StewRecipe(
                    recipe.ingredients,
                    FluidStack.EMPTY,
                    recipe.cookTime,
                    recipe.getResultItem(level.registryAccess()),
                    Ingredient.of(recipe.outputContainer),
                    recipe.experience.roundToInt()
                ))
            }
        }
    }

}