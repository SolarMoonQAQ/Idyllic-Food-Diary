package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.KettleRecipe;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KettleRecipeSerializer implements RecipeSerializer<KettleRecipe> {

    @Override
    public @NotNull KettleRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        Fluid input = SerializeHelper.readFluid(json, "input");
        final int time = GsonHelper.getAsInt(json, "time");
        Fluid output = SerializeHelper.readFluid(json, "output");
        return new KettleRecipe(recipeId, input, time, output);
    }

    @Nullable
    @Override
    public KettleRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        Fluid input = SerializeHelper.readFluid(buffer);
        int time = buffer.readInt();
        Fluid output = SerializeHelper.readFluid(buffer);
        return new KettleRecipe(recipeId, input, time, output);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, KettleRecipe recipe) {
        SerializeHelper.writeFluid(buffer, recipe.input());
        buffer.writeInt(recipe.time());
        SerializeHelper.writeFluid(buffer, recipe.output());
    }

}
