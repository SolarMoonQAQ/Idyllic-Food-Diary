package cn.solarmoon.idyllic_food_diary.compat.farmersdelight.util;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpiceList;
import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.StewRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

public class FarmersUtil {

    /**
     * @return 该方块是否为农夫乐事的热源方块
     */
    public static boolean isHeatSource(BlockState state) {
        return FarmersDelight.isLoaded() && state.is(ModTags.HEAT_SOURCES);
    }

    /**
     * 把所有配方添加到新的烹饪锅配方集合中，以兼容农夫乐事的所有烹饪锅配方
     */
    public static void addAllRecipeToIFDCookingPotRecipe(List<StewRecipe> combination, Level level) {
        if (FarmersDelight.isLoaded()) {
            List<vectorwing.farmersdelight.common.crafting.CookingPotRecipe> fdRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get());
            for (var fdRecipe : fdRecipes) {
                combination.add(new StewRecipe(
                        fdRecipe.getId(),
                        fdRecipe.getIngredients(),
                        FluidStack.EMPTY,
                        new SpiceList(),
                        fdRecipe.getCookTime(),
                        fdRecipe.getResultItem(level.registryAccess()),
                        Ingredient.of(fdRecipe.getOutputContainer()),
                        Math.round(fdRecipe.getExperience())
                ));
            }
        }
    }

}
