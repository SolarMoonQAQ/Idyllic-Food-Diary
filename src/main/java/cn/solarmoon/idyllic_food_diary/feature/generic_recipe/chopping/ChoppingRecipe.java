package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.chopping;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.ChanceResult;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ChoppingRecipe(
        ResourceLocation id,
        Ingredient input,
        NonNullList<ChanceResult> chanceResults
) implements IConcreteRecipe {

    public Ingredient getInput() {
        return this.input;
    }

    public List<ItemStack> getResults() {
        return ChanceResult.getResults(chanceResults);
    }

    public List<ItemStack> getRolledResults(Player player) {
        return ChanceResult.getRolledResults(player, chanceResults);
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.CHOPPING;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public static class Serializer implements RecipeSerializer<ChoppingRecipe> {

        @Override
        public @NotNull ChoppingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("ingredient"));
            NonNullList<ChanceResult> results = SerializeHelper.readChanceResults(json, "result");;
            return new ChoppingRecipe(recipeId, input, results);
        }

        @Nullable
        @Override
        public ChoppingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            NonNullList<ChanceResult> resultsIn = SerializeHelper.readChanceResults(buffer);
            return new ChoppingRecipe(recipeId, input, resultsIn);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, ChoppingRecipe recipe) {
            recipe.input().toNetwork(buffer);
            SerializeHelper.writeChanceResults(buffer, recipe.chanceResults());
        }

    }

}
