package cn.solarmoon.immersive_delight.data.fluid_foods;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.data.fluid_effects.FluidEffectsBuilder;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_foods.serializer.FluidFood;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class FluidFoodsBuilder extends SimpleJsonResourceReloadListener {

    private static final Gson gson = new GsonBuilder().create();
    private static final String directory = "fluid_foods";
    public static final FluidFoodsBuilder INSTANCE;

    public FluidFoodsBuilder() {
        super(gson, directory);
    }

    static {
        INSTANCE = new FluidFoodsBuilder();
    }

    /**
     * 读取json中的id和总的FluidEffect，将每一份对应关系存入map中
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        FluidFood.MAP.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            FluidFood fluidFood = gson.fromJson(entry.getValue(), FluidFood.class);
            FluidFood.MAP.put(fluidFood.getFluid(), fluidFood);
            FluidFood.MAP2.put(fluidFood.getFood(), fluidFood);
            fluidFood.validate();
        }
        ImmersiveDelight.LOGGER.info("foods have been added for {} types of fluids.", FluidFood.MAP.size());
    }

}
