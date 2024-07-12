package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.rolling;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public record RollingRecipe(
        ResourceLocation id,
        Ingredient input,
        int time,
        Block output,
        NonNullList<ChanceResult> chanceResults
) implements IConcreteRecipe {

    public List<ItemStack> getResults() {
        return ChanceResult.getResults(chanceResults);
    }

    public List<ItemStack> getRolledResults(Player player) {
        return ChanceResult.getRolledResults(player, chanceResults);
    }

    public boolean hasBlockOutput() {
        return output != Blocks.AIR;
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.ROLLING;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public static class Serializer implements RecipeSerializer<RollingRecipe> {

        @Override
        public @NotNull RollingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("ingredient"));
            int time = GsonHelper.getAsInt(json, "time");
            Block output = SerializeHelper.readBlock(json, "output", Blocks.AIR);
            NonNullList<ChanceResult> results = SerializeHelper.readChanceResults(json, "result");
            return new RollingRecipe(recipeId, input, time, output, results);
        }

        @Nullable
        @Override
        public RollingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int time = buffer.readInt();
            Block output = SerializeHelper.readBlock(buffer);
            NonNullList<ChanceResult> resultsIn = SerializeHelper.readChanceResults(buffer);
            return new RollingRecipe(recipeId, input, time, output, resultsIn);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, RollingRecipe recipe) {
            recipe.input().toNetwork(buffer);
            buffer.writeInt(recipe.time());
            SerializeHelper.writeBlock(buffer, recipe.output());
            SerializeHelper.writeChanceResults(buffer, recipe.chanceResults());
        }

    }
}

