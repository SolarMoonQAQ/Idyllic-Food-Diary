package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public record SoupPotRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        FluidStack inputFluid,
        int time,
        FluidStack outputFluid,
        List<ItemStack> outputItems,
        boolean compareNBT
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.SOUP_POT;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}