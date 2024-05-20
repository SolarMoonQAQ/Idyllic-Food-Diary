package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record IngredientHandlingRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        Ingredient container,
        boolean isInOrder,
        ItemStack result
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.INGREDIENT_HANDLING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

}
