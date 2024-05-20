package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public record CookingPotRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        FluidStack inputFluid,
        List<Spice> withSpices,
        int time,
        FluidStack outputFluid,
        ItemStack result,
        Ingredient container
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.COOKING_POT;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}