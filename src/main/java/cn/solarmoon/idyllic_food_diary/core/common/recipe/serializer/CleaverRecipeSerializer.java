package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.CleaverRecipe;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.util.RecipeSerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CleaverRecipeSerializer implements RecipeSerializer<CleaverRecipe> {

    @Override
    public @NotNull CleaverRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("ingredient"));
        NonNullList<ChanceResult> results = RecipeSerializeHelper.readChanceResults(json, "result");;
        return new CleaverRecipe(recipeId, input, results);
    }

    @Nullable
    @Override
    public CleaverRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        NonNullList<ChanceResult> resultsIn = RecipeSerializeHelper.readChanceResults(buffer);
        return new CleaverRecipe(recipeId, input, resultsIn);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, CleaverRecipe recipe) {
        recipe.input().toNetwork(buffer);
        RecipeSerializeHelper.writeChanceResults(buffer, recipe.chanceResults());
    }

}
