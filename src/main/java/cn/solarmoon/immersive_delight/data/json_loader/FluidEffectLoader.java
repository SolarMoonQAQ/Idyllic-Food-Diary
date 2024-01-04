package cn.solarmoon.immersive_delight.data.json_loader;

import cn.solarmoon.immersive_delight.data.FluidEffect;
import cn.solarmoon.immersive_delight.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class FluidEffectLoader extends SimpleJsonResourceReloadListener {

    private static final Gson gson = new GsonBuilder().create();
    private static final String directory = "fluid_effects";
    public static final FluidEffectLoader INSTANCE;

    public FluidEffectLoader() {
        super(gson, directory);
    }

    static {
        INSTANCE = new FluidEffectLoader();
    }

    /**
     * 读取json中的id和总的FluidEffect，将每一份对应关系存入map中
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        FluidEffect.MAP.clear();
        for(Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            FluidEffect fluidEffect = gson.fromJson(entry.getValue(), FluidEffect.class);
            FluidEffect.MAP.put(fluidEffect.fluidId, fluidEffect);
            fluidEffect.validate();
        }
        Constants.LOGGER.info("已为 {} 种液体添加效果" + "\nEffects have been added for {} types of fluids.", FluidEffect.MAP.size(), FluidEffect.MAP.size());
    }

}
