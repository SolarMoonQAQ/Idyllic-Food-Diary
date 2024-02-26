package cn.solarmoon.immersive_delight.common.recipe;

import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SteamerRecipe(
        ResourceLocation id,
        Ingredient input,
        int time,
        Item output
) implements Recipe<RecipeWrapper> {


    @Override
    public boolean matches(RecipeWrapper p_44002_, Level p_44003_) {
        return true;
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
        return IMRecipes.STEAMER.getSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return IMRecipes.STEAMER.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SteamerRecipe that = (SteamerRecipe) o;

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


    //序列化编码解码
    public static class Serializer implements RecipeSerializer<SteamerRecipe> {

        public Serializer() {
        }

        @Override
        public @NotNull SteamerRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

            Ingredient input = Ingredient.fromJson(json.get("input"));

            int time = GsonHelper.getAsInt(json, "time");

            Item output = GsonHelper.getAsItem(json, "output");

            return new SteamerRecipe(recipeId, input, time, output);
        }

        @Nullable
        @Override
        public SteamerRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {

            Ingredient input = Ingredient.fromNetwork(buffer);

            int time = buffer.readInt();

            Item output = buffer.readItem().getItem();

            return new SteamerRecipe(recipeId, input, time, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, SteamerRecipe recipe) {

            recipe.input.toNetwork(buffer);

            buffer.writeInt(recipe.time);

            buffer.writeItem(recipe.output.getDefaultInstance());

        }

    }

}
