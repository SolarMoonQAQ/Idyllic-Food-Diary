package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.SteamerRecipe;
import cn.solarmoon.solarmoon_core.api.util.RecipeSerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SteamerRecipeSerializer implements RecipeSerializer<SteamerRecipe> {

    @Override
    public @NotNull SteamerRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("ingredient"));
        int time = GsonHelper.getAsInt(json, "time");
        ItemStack output = RecipeSerializeHelper.readItemStack(json, "result");
        return new SteamerRecipe(recipeId, input, time, output);
    }

    @Nullable
    @Override
    public SteamerRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        int time = buffer.readInt();
        ItemStack output = buffer.readItem();
        return new SteamerRecipe(recipeId, input, time, output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, SteamerRecipe recipe) {
        recipe.input().toNetwork(buffer);
        buffer.writeInt(recipe.time());
        buffer.writeItem(recipe.output());
    }

}
