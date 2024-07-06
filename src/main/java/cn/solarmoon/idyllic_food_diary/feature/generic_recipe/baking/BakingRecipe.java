package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.chopping.ChoppingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.ChanceResult;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BakingRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        int time,
        ItemStack result
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.BAKING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<BakingRecipe> {

        @Override
        public @NotNull BakingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient input = SerializeHelper.readIngredient(json, "ingredient");
            int time = GsonHelper.getAsInt(json, "time", 0);
            ItemStack result = SerializeHelper.readItemStack(json, "result");
            return new BakingRecipe(recipeId, input, time, result);
        }

        @Nullable
        @Override
        public BakingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            int time = buf.readInt();
            ItemStack result = SerializeHelper.readItemStack(buf);
            return new BakingRecipe(recipeId, input, time, result);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, BakingRecipe recipe) {
            recipe.ingredient.toNetwork(buf);
            buf.writeInt(recipe.time);
            SerializeHelper.writeItemStack(buf, recipe.result);
        }

    }

}
