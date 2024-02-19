package cn.solarmoon.immersive_delight.common.recipe;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractTinFoilBoxBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record TinFoilGrillingRecipe(
        ResourceLocation id,
        List<Ingredient> input,
        int time,
        String output
) implements Recipe<RecipeWrapper> {

    /**
     * 输入物和内容物完全匹配<br/>
     * 以及下方需要热源
     */
    public boolean inputMatches(AbstractTinFoilBoxBlockEntity tinFoil, BlockPos pos, Level level) {
        List<ItemStack> stacks = tinFoil.getStacks();
        if (RecipeMatcher.findMatches(stacks, input) != null) {
            return FarmerUtil.isHeatSource(level.getBlockState(pos.below()));
        }
        return false;
    }

    @Nullable
    public BlockState getOutput() {
        Block output = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(output()));
        if (output != null) {
            return output.defaultBlockState();
        }
        return null;
    }

    @Override
    public boolean matches(RecipeWrapper p_44002_, Level p_44003_) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeWrapper p_44001_, RegistryAccess p_267165_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return IMRecipes.TIN_FOIL_GRILLING.getSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return IMRecipes.TIN_FOIL_GRILLING.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TinFoilGrillingRecipe that = (TinFoilGrillingRecipe) o;

        if (!id.equals(that.id)) return false;
        if (!input.equals(that.input)) return false;
        if (time != that.time) return false;
        return output.equals(that.output);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + input.hashCode();
        result = 31 * result + time;
        result = 31 * result + output.hashCode();
        return result;
    }

    public static class Serializer implements RecipeSerializer<TinFoilGrillingRecipe> {

        public Serializer() {
        }

        @Override
        public TinFoilGrillingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            List<Ingredient> input = new ArrayList<>();
            JsonArray inArray = GsonHelper.getAsJsonArray(json, "input");
            for (var element : inArray) {
                input.add(Ingredient.fromJson(element));
            }

            int time = GsonHelper.getAsInt(json, "time");

            String output = GsonHelper.getAsString(json, "output");

            return new TinFoilGrillingRecipe(recipeId, input, time, output);
        }

        @Override
        public TinFoilGrillingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            List<Ingredient> input = new ArrayList<>();
            int inCount = buffer.readVarInt();
            for (int i = 0; i < inCount; i++) {
                input.add(Ingredient.fromNetwork(buffer));
            }

            int time = buffer.readInt();

            String output = buffer.readUtf();

            return new TinFoilGrillingRecipe(recipeId, input, time, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, TinFoilGrillingRecipe recipe) {

            for (var in : recipe.input) {
                in.toNetwork(buffer);
            }

            buffer.writeInt(recipe.time);

            buffer.writeUtf(recipe.output);

        }

    }

}
