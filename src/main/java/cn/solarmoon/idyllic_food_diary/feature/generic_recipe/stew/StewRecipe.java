package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
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


public record StewRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        FluidStack inputFluid,
        SpiceList withSpices,
        int time,
        ItemStack result,
        Ingredient container,
        int exp
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.STEW;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public static class Serializer implements RecipeSerializer<StewRecipe> {

        @Override
        public @NotNull StewRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            List<Ingredient> inputIngredients = SerializeHelper.readIngredients(json, "ingredients");
            FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
            SpiceList withSpices = SpiceList.readSpices(json, "with_spices");
            int time = GsonHelper.getAsInt(json, "time");
            ItemStack result = SerializeHelper.readItemStack(json, "result", ItemStack.EMPTY);
            Ingredient container = SerializeHelper.readIngredient(json, "container");
            int exp = GsonHelper.getAsInt(json, "exp", 0);
            return new StewRecipe(recipeId, inputIngredients, inputFluid, withSpices, time, result, container, exp);
        }

        @Nullable
        @Override
        public StewRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            List<Ingredient> ingredients = SerializeHelper.readIngredients(buffer);
            FluidStack inputFluid = buffer.readFluidStack();
            SpiceList withSpices = SpiceList.readSpices(buffer);
            int time = buffer.readInt();
            ItemStack result = SerializeHelper.readItemStack(buffer);
            Ingredient container = Ingredient.fromNetwork(buffer);
            int exp = buffer.readInt();
            return new StewRecipe(recipeId, ingredients, inputFluid, withSpices, time, result, container, exp);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, StewRecipe recipe) {
            SerializeHelper.writeIngredients(buffer, recipe.ingredients());
            buffer.writeFluidStack(recipe.inputFluid());
            SpiceList.writeSpices(buffer, recipe.withSpices());
            buffer.writeInt(recipe.time());
            SerializeHelper.writeItemStack(buffer, recipe.result());
            recipe.container().toNetwork(buffer);
            buffer.writeInt(recipe.exp());
        }

    }
}