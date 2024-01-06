package cn.solarmoon.immersive_delight.common.recipes.helper;

import cn.solarmoon.immersive_delight.common.IMRecipes;
import cn.solarmoon.immersive_delight.common.recipes.KettleRecipe;
import cn.solarmoon.immersive_delight.common.recipes.RollingPinRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class GetRecipes {

    /**
     * 获取擀面杖所有配方
     */
    public static List<RollingPinRecipe> rollingRecipes(Level level) {
        List<RollingPinRecipe> allRollingRecipes;
        //动态遍历擀面杖配方
        RecipeManager recipeManager = level.getRecipeManager();
        allRollingRecipes = recipeManager.getAllRecipesFor(IMRecipes.ROLLING_RECIPE.get()).stream()
                .filter(Objects::nonNull)
                .toList();
        return allRollingRecipes;
    }

    /**
     * 获取烧水壶所有配方
     */
    public static List<KettleRecipe> kettleRecipes(Level level) {
        List<KettleRecipe> allKettleRecipes;
        RecipeManager recipeManager = level.getRecipeManager();
        allKettleRecipes = recipeManager.getAllRecipesFor(IMRecipes.KETTLE_RECIPE.get()).stream()
                .filter(Objects::nonNull)
                .toList();
        return allKettleRecipes;
    }

}
