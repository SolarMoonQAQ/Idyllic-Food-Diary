package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.CupRecipe;
import cn.solarmoon.solarmoon_core.api.util.RecipeSerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CupRecipeSerializer implements RecipeSerializer<CupRecipe> {

    @Override
    public @NotNull CupRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        FluidStack inputFluid = RecipeSerializeHelper.readFluidStack(json, "input_fluid");
        final int time = GsonHelper.getAsInt(json, "time");
        FluidStack outputFluid = RecipeSerializeHelper.readFluidStack(json, "output_fluid");
        boolean compareNBT = GsonHelper.getAsBoolean(json, "compare_nbt", false);
        return new CupRecipe(recipeId, ingredient, inputFluid, time, outputFluid, compareNBT);
    }

    @Nullable
    @Override
    public CupRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        Ingredient inputIngredient = Ingredient.fromNetwork(buffer);
        FluidStack inputFluid = buffer.readFluidStack();
        int time = buffer.readInt();
        FluidStack outputFluid = buffer.readFluidStack();
        boolean compareNBT = buffer.readBoolean();
        return new CupRecipe(recipeId, inputIngredient, inputFluid, time, outputFluid, compareNBT);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, CupRecipe recipe) {
        recipe.ingredient().toNetwork(buffer);
        buffer.writeFluidStack(recipe.inputFluid());
        buffer.writeInt(recipe.time());
        buffer.writeFluidStack(recipe.outputFluid());
        buffer.writeBoolean(recipe.compareNBT());
    }

}
