package cn.solarmoon.idyllic_food_diary.core.common.recipe.serializer;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupServingRecipe;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class SoupServingRecipeSerializer implements RecipeSerializer<SoupServingRecipe> {

    @Override
    public SoupServingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        ItemStack container = CraftingHelper.getItemStack(json.getAsJsonObject("container"), true);
        FluidStack fluidToServe = SerializeHelper.readFluidStack(json, "fluid");
        ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
        return new SoupServingRecipe(recipeId, container, fluidToServe, result);
    }

    @Nullable
    @Override
    public SoupServingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        ItemStack container = buffer.readItem();
        FluidStack fluidToServe = buffer.readFluidStack();
        ItemStack result = buffer.readItem();
        return new SoupServingRecipe(recipeId, container, fluidToServe, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, SoupServingRecipe recipe) {
        buffer.writeItemStack(recipe.container(), false);
        buffer.writeFluidStack(recipe.fluidToServe());
        buffer.writeItemStack(recipe.result(), false);
    }

}
