package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupPotRecipe;
import cn.solarmoon.solarmoon_core.api.util.RecipeSerializeHelper;
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

public class SoupPotRecipeSerializer implements RecipeSerializer<SoupPotRecipe> {

    @Override
    public @NotNull SoupPotRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        List<Ingredient> inputIngredients = RecipeSerializeHelper.readIngredients(json, "ingredients");
        FluidStack inputFluid = RecipeSerializeHelper.readFluidStack(json, "input_fluid");
        int time = GsonHelper.getAsInt(json, "time");
        FluidStack outputFluid = RecipeSerializeHelper.readFluidStack(json, "output_fluid");
        List<ItemStack> outputItems = RecipeSerializeHelper.readItemStacks(json, "output_items");
        boolean compareNBT = GsonHelper.getAsBoolean(json, "compare_nbt", false);
        return new SoupPotRecipe(recipeId, inputIngredients, inputFluid, time, outputFluid, outputItems, compareNBT);
    }

    @Nullable
    @Override
    public SoupPotRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        List<Ingredient> ingredients = RecipeSerializeHelper.readIngredients(buffer);
        FluidStack inputFluid = buffer.readFluidStack();
        int time = buffer.readInt();
        FluidStack outputFluid = buffer.readFluidStack();
        List<ItemStack> outputItems = RecipeSerializeHelper.readItemStacks(buffer);
        boolean compareNBT = buffer.readBoolean();
        return new SoupPotRecipe(recipeId, ingredients, inputFluid, time, outputFluid, outputItems, compareNBT);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, SoupPotRecipe recipe) {
        RecipeSerializeHelper.writeIngredients(buffer, recipe.ingredients());
        buffer.writeFluidStack(recipe.inputFluid());
        buffer.writeInt(recipe.time());
        buffer.writeFluidStack(recipe.outputFluid());
        RecipeSerializeHelper.writeItemStacks(buffer, recipe.outputItems());
        buffer.writeBoolean(recipe.compareNBT());
    }

}
