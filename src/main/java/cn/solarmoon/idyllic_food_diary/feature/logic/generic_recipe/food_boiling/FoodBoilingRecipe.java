package cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.food_boiling;

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
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public record FoodBoilingRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        FluidStack fluidConsumption,
        int time,
        ItemStack result
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.FOOD_BOILING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<FoodBoilingRecipe> {

        @Override
        public FoodBoilingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = SerializeHelper.readIngredient(json, "ingredient");
            FluidStack fluidConsumption = SerializeHelper.readFluidStack(json, "fluid_consumption");
            int time = GsonHelper.getAsInt(json, "time");
            ItemStack result = SerializeHelper.readItemStack(json, "result");
            return new FoodBoilingRecipe(id, ingredient, fluidConsumption, time, result);
        }

        @Override
        public @Nullable FoodBoilingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            FluidStack fluidConsumption = buf.readFluidStack();
            int time = buf.readInt();
            ItemStack result = SerializeHelper.readItemStack(buf);
            return new FoodBoilingRecipe(id, ingredient, fluidConsumption, time, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FoodBoilingRecipe recipe) {
            recipe.ingredient().toNetwork(buf);
            buf.writeFluidStack(recipe.fluidConsumption());
            buf.writeInt(recipe.time());
            SerializeHelper.writeItemStack(buf, recipe.result());
        }

    }

}
