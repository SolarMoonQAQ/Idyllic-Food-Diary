package cn.solarmoon.immersive_delight.common.recipes;

import cn.solarmoon.immersive_delight.util.RecipeHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.solarmoon.immersive_delight.common.IMRecipes.*;

public class CleaverRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final NonNullList<RecipeHelper.ChanceResult> results;

    public CleaverRecipe(ResourceLocation id, Ingredient input, NonNullList<RecipeHelper.ChanceResult> results) {
        this.id = id;
        this.input = input;
        this.results = results;
    }

    /**
     * 单个输入物品匹配
     */
    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
        return input.test(inv.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper inv, @NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getResults() {
        return getRollableResults().stream()
                .map(RecipeHelper.ChanceResult::stack)
                .collect(Collectors.toList());
    }

    public NonNullList<RecipeHelper.ChanceResult> getRollableResults() {
        return this.results;
    }

    /**
     * 根据幸运等级对results进行随机选取并输出最终结果
     */
    public List<ItemStack> rollResults(RandomSource rand, int fortuneLevel) {
        List<ItemStack> results = new ArrayList<>();
        NonNullList<RecipeHelper.ChanceResult> rollableResults = getRollableResults();
        for (RecipeHelper.ChanceResult output : rollableResults) {
            ItemStack stack = output.rollOutput(rand, fortuneLevel);
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CLEAVER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CLEAVER_RECIPE.get();
    }

    //配方哈希值比较
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CleaverRecipe that = (CleaverRecipe) o;

        if (!id.equals(that.id)) return false;
        if (!input.equals(that.input)) return false;
        return getResults().equals(that.getResults());
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + input.hashCode();
        result = 31 * result + getResults().hashCode();
        return result;
    }


    //序列化编码解码
    public static class Serializer implements RecipeSerializer<CleaverRecipe> {

        public Serializer() {
        }

        private static NonNullList<RecipeHelper.ChanceResult> readResults(JsonArray resultArray) {
            NonNullList<RecipeHelper.ChanceResult> results = NonNullList.create();
            for (JsonElement result : resultArray) {
                results.add(RecipeHelper.ChanceResult.deserialize(result));
            }
            return results;
        }

        @Override
        public @NotNull CleaverRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            Ingredient input = Ingredient.fromJson(json.get("input"));

            NonNullList<RecipeHelper.ChanceResult> results = NonNullList.create();
            if (json.has("result")) {
                results = readResults(GsonHelper.getAsJsonArray(json, "result"));
            }

            return new CleaverRecipe(recipeId, input, results);
        }

        @Nullable
        @Override
        public CleaverRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {

            Ingredient input = Ingredient.fromNetwork(buffer);

            int i = buffer.readVarInt();
            NonNullList<RecipeHelper.ChanceResult> resultsIn = NonNullList.withSize(i, RecipeHelper.ChanceResult.EMPTY);
            resultsIn.replaceAll(ignored -> RecipeHelper.ChanceResult.read(buffer));

            return new CleaverRecipe(recipeId, input, resultsIn);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, CleaverRecipe recipe) {

            recipe.input.toNetwork(buffer);

            buffer.writeVarInt(recipe.results.size());
            for (RecipeHelper.ChanceResult result : recipe.results) {
                result.write(buffer);
            }

        }

    }
}
