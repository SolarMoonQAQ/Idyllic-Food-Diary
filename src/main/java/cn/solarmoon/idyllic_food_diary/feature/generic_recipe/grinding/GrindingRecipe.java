package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grinding;

import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public record GrindingRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        FluidStack inputFluid,
        int time,
        ItemStack result,
        FluidStack outputFluid
) implements IConcreteRecipe {
    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.GRINDING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<GrindingRecipe> {

        @Override
        public GrindingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = SerializeHelper.readIngredient(json, "ingredient");
            FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
            int time = GsonHelper.getAsInt(json, "time");
            ItemStack result = SerializeHelper.readItemStack(json, "result");
            FluidStack fluidOutput = SerializeHelper.readFluidStack(json, "output_fluid");
            return new GrindingRecipe(id, ingredient, inputFluid, time, result, fluidOutput);
        }

        @Override
        public @Nullable GrindingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            FluidStack inputFluid = buf.readFluidStack();
            int time = buf.readInt();
            ItemStack result = SerializeHelper.readItemStack(buf);
            FluidStack fluidOutput = buf.readFluidStack();
            return  new GrindingRecipe(id, ingredient, inputFluid, time, result, fluidOutput);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GrindingRecipe recipe) {
            recipe.ingredient().toNetwork(buf);
            buf.writeFluidStack(recipe.inputFluid);
            buf.writeInt(recipe.time());
            SerializeHelper.writeItemStack(buf, recipe.result());
            buf.writeFluidStack(recipe.outputFluid);
        }

    }
}
