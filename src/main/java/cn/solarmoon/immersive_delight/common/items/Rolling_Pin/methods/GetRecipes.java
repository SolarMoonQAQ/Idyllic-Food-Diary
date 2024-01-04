package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods;

import cn.solarmoon.immersive_delight.common.RegisterItems;
import cn.solarmoon.immersive_delight.common.items.Rolling_Pin.recipe.RollingPinRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class GetRecipes {

    /**
     * 获取擀面杖所有配方
     */
    public static List<RollingPinRecipe> getRollingRecipes(Level level) {
        List<RollingPinRecipe> allRollingRecipes;
        //动态遍历擀面杖配方
        RecipeManager recipeManager = level.getRecipeManager();
        allRollingRecipes = recipeManager.getAllRecipesFor(RegisterItems.ROLLING.get()).stream()
                .filter(Objects::nonNull)
                .toList();
        return allRollingRecipes;
    }

}
