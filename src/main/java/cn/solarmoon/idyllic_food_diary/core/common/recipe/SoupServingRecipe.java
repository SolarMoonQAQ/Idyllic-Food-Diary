package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public record SoupServingRecipe(
        ResourceLocation id,
        Ingredient container,
        FluidStack fluidToServe,
        ItemStack result
) implements IConcreteRecipe {

    public int getAmountToServe() {
        return fluidToServe.getAmount();
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.SOUP_SERVING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

}
