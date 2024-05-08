package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record CupRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        FluidStack inputFluid,
        int time,
        FluidStack outputFluid,
        boolean compareNBT

) implements IConcreteRecipe {

    /**
     * 获取设定的流体量
     */
    public int getInputAmount() {
        return inputFluid.getAmount();
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.CUP;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

}
