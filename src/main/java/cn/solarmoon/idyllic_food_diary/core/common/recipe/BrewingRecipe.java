package cn.solarmoon.idyllic_food_diary.core.common.recipe;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BrewingRecipe(
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
        return IMRecipes.BREWING;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public static class Serializer implements RecipeSerializer<BrewingRecipe> {

        @Override
        public @NotNull BrewingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
            final int time = GsonHelper.getAsInt(json, "time");
            FluidStack outputFluid = SerializeHelper.readFluidStack(json, "output_fluid");
            boolean compareNBT = GsonHelper.getAsBoolean(json, "compare_nbt", false);
            return new BrewingRecipe(recipeId, ingredient, inputFluid, time, outputFluid, compareNBT);
        }

        @Nullable
        @Override
        public BrewingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
            FluidStack inputFluid = buffer.readFluidStack();
            int time = buffer.readInt();
            FluidStack outputFluid = buffer.readFluidStack();
            boolean compareNBT = buffer.readBoolean();
            return new BrewingRecipe(recipeId, inputIngredient, inputFluid, time, outputFluid, compareNBT);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, BrewingRecipe recipe) {
            recipe.ingredient().toNetwork(buffer);
            buffer.writeFluidStack(recipe.inputFluid());
            buffer.writeInt(recipe.time());
            buffer.writeFluidStack(recipe.outputFluid());
            buffer.writeBoolean(recipe.compareNBT());
        }

    }
}
