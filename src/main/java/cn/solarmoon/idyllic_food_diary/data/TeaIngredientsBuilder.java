package cn.solarmoon.idyllic_food_diary.data;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaIngredient;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaIngredientXX;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class TeaIngredientsBuilder extends SimpleJsonResourceReloadListener {

    private static final Gson gson = new GsonBuilder().create();
    private static final String directory = "tea_ingredients";

    public TeaIngredientsBuilder() {
        super(gson, directory);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        TeaIngredient.ALL.clear();
        for (var entry : map.entrySet()) {
            TeaIngredient.Type teaType = gson.fromJson(entry.getValue(), TeaIngredient.class).getType();
            TeaIngredient teaIngredient = switch (teaType) {
                case BASE -> SerializeHelper.GSON.fromJson(entry.getValue(), TeaIngredient.Base.class);
                case SIDE -> SerializeHelper.GSON.fromJson(entry.getValue(), TeaIngredient.Side.class);
                case ADD -> SerializeHelper.GSON.fromJson(entry.getValue(), TeaIngredient.Add.class);
            };
            TeaIngredient.ALL.add(teaIngredient);
        }
        IdyllicFoodDiary.DEBUG.getLogger().info("Successfully loaded {} Base / {} Add / {} Side Tea Ingredients",
                TeaIngredient.ALL.getBaseIngredients().size(),
                TeaIngredient.ALL.getAddIngredients().size(),
                TeaIngredient.ALL.getSideIngredients().size()
        );
    }

}
