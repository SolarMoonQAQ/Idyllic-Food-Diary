package cn.solarmoon.idyllic_food_diary.core.data.builder;

import cn.solarmoon.idyllic_food_diary.api.tea_brewing.TeaIngredient;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
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
            TeaIngredient teaIngredient = gson.fromJson(entry.getValue(), TeaIngredient.class);
            teaIngredient.put();
            teaIngredient.validate();
        }
        IdyllicFoodDiary.DEBUG.getLogger().info("Successfully loaded {} Tea ingredients", TeaIngredient.ALL.size());
    }

}
