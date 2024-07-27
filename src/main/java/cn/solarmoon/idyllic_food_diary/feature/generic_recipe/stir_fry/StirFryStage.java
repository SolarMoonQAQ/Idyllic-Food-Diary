package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public record StirFryStage(
        List<Ingredient> ingredients,
        FluidStack fluidStack,
        SpiceList spices,
        int time,
        int fryCount,
        boolean keepFluid
) {

    public static StirFryStage readFromJson(JsonObject json) {
        List<Ingredient> ingredients = SerializeHelper.readIngredients(json, "ingredients");
        FluidStack fluidStack = SerializeHelper.readFluidStack(json, "fluid");
        SpiceList spices = SpiceList.readSpices(json, "with_spices");
        int time = GsonHelper.getAsInt(json, "time", 0);
        int fryCount = GsonHelper.getAsInt(json, "fry_count", 0);
        boolean keepFluid = GsonHelper.getAsBoolean(json, "keep_fluid", true);
        return new StirFryStage(ingredients, fluidStack, spices, time, fryCount, keepFluid);
    }

    public static StirFryStage readFromNetwork(FriendlyByteBuf buf) {
        List<Ingredient> ingredients = SerializeHelper.readIngredients(buf);
        FluidStack fluidStack = buf.readFluidStack();
        SpiceList spices = SpiceList.readSpices(buf);
        int time = buf.readInt();
        int fryCount = buf.readInt();
        boolean keepFluid = buf.readBoolean();
        return new StirFryStage(ingredients, fluidStack, spices, time, fryCount, keepFluid);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        SerializeHelper.writeIngredients(buf, ingredients);
        buf.writeFluidStack(fluidStack);
        SpiceList.writeSpices(buf, spices);
        buf.writeInt(time);
        buf.writeInt(fryCount);
        buf.writeBoolean(keepFluid);
    }

    public static List<StirFryStage> readListFromJson(JsonObject json, String id) {
        if (json.has(id)) {
            List<StirFryStage> stirFryStages = new ArrayList<>();
            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "stir_fry_stages");
            for (var element : jsonArray) {
                stirFryStages.add(readFromJson(element.getAsJsonObject()));
            }
            return stirFryStages;
        }
        return new ArrayList<>();
    }

    public static List<StirFryStage> readListFromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<StirFryStage> stirFryStages = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            stirFryStages.add(readFromNetwork(buf));
        }
        return stirFryStages;
    }

    public static void writeListToNetwork(FriendlyByteBuf buf, List<StirFryStage> stirFryStages) {
        buf.writeVarInt(stirFryStages.size());
        for (var stage : stirFryStages) {
            stage.toNetwork(buf);
        }
    }

}
