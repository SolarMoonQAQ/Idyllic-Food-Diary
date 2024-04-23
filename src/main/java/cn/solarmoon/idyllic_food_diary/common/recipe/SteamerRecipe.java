package cn.solarmoon.idyllic_food_diary.common.recipe;

import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.registry.object.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record SteamerRecipe(
        ResourceLocation id,
        Ingredient input,
        int time,
        ItemStack output
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.STEAMER;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

}
