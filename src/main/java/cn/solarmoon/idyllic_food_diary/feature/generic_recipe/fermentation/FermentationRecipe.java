package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.fermentation;

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.recipe.ProportionalIngredient;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record FermentationRecipe (
        ResourceLocation id,
        List<ProportionalIngredient> ingredients,
        FluidStack inputFluid,
        Temp temp,
        SpiceList withSpices,
        int time,
        FluidStack outputFluid
) implements IConcreteRecipe {

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return IMRecipes.FERMENTATION;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<FermentationRecipe> {

        @Override
        public FermentationRecipe fromJson(ResourceLocation id, JsonObject json) {
            List<ProportionalIngredient> inputIngredients = ProportionalIngredient.readProportionalIngredients(json, "proportional_ingredients");
            FluidStack inputFluid = SerializeHelper.readFluidStack(json, "input_fluid");
            Temp tempScale = Temp.readFromJson(json);
            SpiceList withSpices = SpiceList.readSpices(json, "with_spices");
            int time = GsonHelper.getAsInt(json, "time");
            FluidStack outputFluid = SerializeHelper.readFluidStack(json, "output_fluid");
            return new FermentationRecipe(id, inputIngredients, inputFluid, tempScale, withSpices, time, outputFluid);
        }

        @Override
        public @Nullable FermentationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            List<ProportionalIngredient> ingredients = ProportionalIngredient.readProportionalIngredients(buf);
            FluidStack inputFluid = buf.readFluidStack();
            Temp tempScale = buf.readEnum(Temp.class);
            SpiceList withSpices = SpiceList.readSpices(buf);
            int time = buf.readInt();
            FluidStack outputFluid = buf.readFluidStack();
            return new FermentationRecipe(id, ingredients, inputFluid, tempScale, withSpices, time, outputFluid);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FermentationRecipe recipe) {
            ProportionalIngredient.writeProportionalIngredients(buf, recipe.ingredients);
            buf.writeFluidStack(recipe.inputFluid());
            buf.writeEnum(recipe.temp);
            SpiceList.writeSpices(buf, recipe.withSpices());
            buf.writeInt(recipe.time());
            recipe.outputFluid().writeToPacket(buf);
        }

    }

}
