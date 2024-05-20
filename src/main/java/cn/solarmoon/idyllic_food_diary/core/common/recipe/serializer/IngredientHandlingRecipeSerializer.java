package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.CupRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.IngredientHandlingRecipe;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IngredientHandlingRecipeSerializer implements RecipeSerializer<IngredientHandlingRecipe> {

    @Override
    public IngredientHandlingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
        List<Ingredient> ingredients = SerializeHelper.readIngredients(jsonObject, "ingredients");
        Ingredient container = SerializeHelper.readIngredient(jsonObject, "container");
        boolean orderOrNot = GsonHelper.getAsBoolean(jsonObject, "order", false);
        ItemStack result = SerializeHelper.readItemStack(jsonObject, "result");
        return new IngredientHandlingRecipe(id, ingredients, container, orderOrNot, result);
    }

    @Override
    public @Nullable IngredientHandlingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        List<Ingredient> ingredients = SerializeHelper.readIngredients(buf);
        Ingredient container = Ingredient.fromNetwork(buf);
        boolean orderOrNot = buf.readBoolean();
        ItemStack result = buf.readItem();
        return new IngredientHandlingRecipe(id, ingredients, container, orderOrNot, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, IngredientHandlingRecipe recipe) {
        SerializeHelper.writeIngredients(buf, recipe.ingredients());
        recipe.container().toNetwork(buf);
        buf.writeBoolean(recipe.isInOrder());
        buf.writeItemStack(recipe.result(), false);
    }

}
