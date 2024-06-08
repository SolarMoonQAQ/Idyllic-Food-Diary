package cn.solarmoon.idyllic_food_diary.feature.logic.ingredient_handling;

import cn.solarmoon.idyllic_food_diary.feature.logic.spice.SpiceList;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record IngredientHandlingRecipe(
        ResourceLocation id,
        List<Ingredient> ingredients,
        SpiceList withSpices,
        Ingredient container,
        boolean isInOrder,
        ItemStack result
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.INGREDIENT_HANDLING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<IngredientHandlingRecipe> {

        @Override
        public IngredientHandlingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            List<Ingredient> ingredients = SerializeHelper.readIngredients(jsonObject, "ingredients");
            SpiceList withSpices = SpiceList.readSpices(jsonObject, "with_spices");
            Ingredient container = SerializeHelper.readIngredient(jsonObject, "container");
            boolean orderOrNot = GsonHelper.getAsBoolean(jsonObject, "order", false);
            ItemStack result = SerializeHelper.readItemStack(jsonObject, "result");
            return new IngredientHandlingRecipe(id, ingredients, withSpices, container, orderOrNot, result);
        }

        @Override
        public @Nullable IngredientHandlingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            List<Ingredient> ingredients = SerializeHelper.readIngredients(buf);
            SpiceList withSpices = SpiceList.readSpices(buf);
            Ingredient container = Ingredient.fromNetwork(buf);
            boolean orderOrNot = buf.readBoolean();
            ItemStack result = buf.readItem();
            return new IngredientHandlingRecipe(id, ingredients, withSpices, container, orderOrNot, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, IngredientHandlingRecipe recipe) {
            SerializeHelper.writeIngredients(buf, recipe.ingredients());
            SpiceList.writeSpices(buf, recipe.withSpices());
            recipe.container().toNetwork(buf);
            buf.writeBoolean(recipe.isInOrder());
            buf.writeItemStack(recipe.result(), false);
        }

    }
}
