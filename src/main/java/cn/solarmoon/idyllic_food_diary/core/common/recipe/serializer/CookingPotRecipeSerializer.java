package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.CookingPotRecipe;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CookingPotRecipeSerializer implements RecipeSerializer<CookingPotRecipe> {

    @Override
    public @NotNull CookingPotRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        List<Ingredient> inputIngredients = SerializeHelper.readIngredients(json, "ingredients");
        FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
        List<Spice> withSpices = Spice.readSpices(json, "with_spices");
        int time = GsonHelper.getAsInt(json, "time");
        FluidStack outputFluid = SerializeHelper.readFluidStack(json, "output_fluid");
        ItemStack result = SerializeHelper.readItemStack(json, "result");
        Ingredient container = SerializeHelper.readIngredient(json, "container");
        return new CookingPotRecipe(recipeId, inputIngredients, inputFluid, withSpices, time, outputFluid, result, container);
    }

    @Nullable
    @Override
    public CookingPotRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        List<Ingredient> ingredients = SerializeHelper.readIngredients(buffer);
        FluidStack inputFluid = buffer.readFluidStack();
        List<Spice> withSpices = Spice.readSpices(buffer);
        int time = buffer.readInt();
        FluidStack outputFluid = buffer.readFluidStack();
        ItemStack result = SerializeHelper.readItemStack(buffer);
        Ingredient container = Ingredient.fromNetwork(buffer);
        return new CookingPotRecipe(recipeId, ingredients, inputFluid, withSpices, time, outputFluid, result, container);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, CookingPotRecipe recipe) {
        SerializeHelper.writeIngredients(buffer, recipe.ingredients());
        buffer.writeFluidStack(recipe.inputFluid());
        Spice.writeSpices(buffer, recipe.withSpices());
        buffer.writeInt(recipe.time());
        buffer.writeFluidStack(recipe.outputFluid());
        SerializeHelper.writeItemStack(buffer, recipe.result());
        recipe.container().toNetwork(buffer);
    }

}
