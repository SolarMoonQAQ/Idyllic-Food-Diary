package cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.steaming;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SteamingRecipe(
        ResourceLocation id,
        Ingredient input,
        int time,
        ItemStack output
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.STEAMING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<SteamingRecipe> {

        @Override
        public @NotNull SteamingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("ingredient"));
            int time = GsonHelper.getAsInt(json, "time");
            ItemStack output = SerializeHelper.readItemStack(json, "result");
            return new SteamingRecipe(recipeId, input, time, output);
        }

        @Nullable
        @Override
        public SteamingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int time = buffer.readInt();
            ItemStack output = buffer.readItem();
            return new SteamingRecipe(recipeId, input, time, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, SteamingRecipe recipe) {
            recipe.input().toNetwork(buffer);
            buffer.writeInt(recipe.time());
            buffer.writeItemStack(recipe.output(), false);
        }

    }
}
