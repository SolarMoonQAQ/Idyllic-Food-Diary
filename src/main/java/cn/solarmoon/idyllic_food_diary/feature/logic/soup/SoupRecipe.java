package cn.solarmoon.idyllic_food_diary.feature.logic.soup;

import cn.solarmoon.idyllic_food_diary.feature.logic.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
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

import java.util.List;

public record SoupRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        FluidStack inputFluid,
        SpiceList withSpices,
        int time,
        FluidStack outputFluid,
        int exp
) implements IConcreteRecipe {
    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.SOUP;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<SoupRecipe> {

        @Override
        public @NotNull SoupRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            List<Ingredient> inputIngredients = SerializeHelper.readIngredients(json, "ingredients");
            FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
            SpiceList withSpices = SpiceList.readSpices(json, "with_spices");
            int time = GsonHelper.getAsInt(json, "time");
            FluidStack outputFluid = SerializeHelper.readFluidStack(json, "output_fluid");
            int exp = GsonHelper.getAsInt(json, "exp", 0);
            return new SoupRecipe(recipeId, inputIngredients, inputFluid, withSpices, time, outputFluid, exp);
        }

        @Nullable
        @Override
        public SoupRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            List<Ingredient> ingredients = SerializeHelper.readIngredients(buffer);
            FluidStack inputFluid = buffer.readFluidStack();
            SpiceList withSpices = SpiceList.readSpices(buffer);
            int time = buffer.readInt();
            FluidStack outputFluid = buffer.readFluidStack();
            int exp = buffer.readInt();
            return new SoupRecipe(recipeId, ingredients, inputFluid, withSpices, time, outputFluid, exp);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, SoupRecipe recipe) {
            SerializeHelper.writeIngredients(buffer, recipe.ingredients());
            buffer.writeFluidStack(recipe.inputFluid());
            SpiceList.writeSpices(buffer, recipe.withSpices());
            buffer.writeInt(recipe.time());
            recipe.outputFluid().writeToPacket(buffer);
            buffer.writeInt(recipe.exp());
        }

    }
}
