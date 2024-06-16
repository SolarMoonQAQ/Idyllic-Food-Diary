package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record StirFryRecipe(
        ResourceLocation id,
        List<StirFryStage> stirFryStages,
        ItemStack result,
        Ingredient container
) implements IConcreteRecipe {

    public StirFryStage getFirstStage() {
        return stirFryStages.get(0);
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.STIR_FRY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<StirFryRecipe> {

        @Override
        public @NotNull StirFryRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            List<StirFryStage> stirFryStages = StirFryStage.readListFromJson(json, "stir_fry_stages");
            ItemStack stack = SerializeHelper.readItemStack(json, "result");
            Ingredient container = SerializeHelper.readIngredient(json, "container");
            return new StirFryRecipe(recipeId, stirFryStages, stack, container);
        }

        @Nullable
        @Override
        public StirFryRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            List<StirFryStage> stirFryStages = StirFryStage.readListFromNetwork(buffer);
            ItemStack stack = SerializeHelper.readItemStack(buffer);
            Ingredient container = Ingredient.fromNetwork(buffer);
            return new StirFryRecipe(recipeId, stirFryStages, stack, container);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, StirFryRecipe recipe) {
            StirFryStage.writeListToNetwork(buffer, recipe.stirFryStages);
            SerializeHelper.writeItemStack(buffer, recipe.result);
            recipe.container.toNetwork(buffer);
        }

    }

}
