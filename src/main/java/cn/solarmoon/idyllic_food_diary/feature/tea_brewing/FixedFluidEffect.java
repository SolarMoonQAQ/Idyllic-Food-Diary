package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.data.FoodValue;
import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FixedFluidEffect {

    public static final List<FixedFluidEffect> ALL = new ArrayList<>();

    public static FixedFluidEffect EMPTY = new FixedFluidEffect(Fluids.EMPTY, new ArrayList<>(), false, 0, false, FoodValue.EMPTY, false);

    private final Fluid fluid;
    private final List<MobEffectInstance> effects;
    private final boolean clear;
    private final int fire;
    private final boolean extinguishing;
    private final FoodValue foodValue;
    private final boolean canAlwaysDrink;

    public FixedFluidEffect(Fluid fluid, List<MobEffectInstance> effects, boolean clear, int fire, boolean extinguishing, FoodValue foodValue, boolean canAlwaysDrink) {
        this.fluid = fluid;
        this.effects = effects;
        this.clear = clear;
        this.fire = fire;
        this.extinguishing = extinguishing;
        this.foodValue = foodValue;
        this.canAlwaysDrink = canAlwaysDrink;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public List<MobEffectInstance> getEffects() {
        List<MobEffectInstance> effectsCopy = new ArrayList<>();
        effects.forEach(effect -> effectsCopy.add(MobEffectInstance.load(effect.save(new CompoundTag()))));
        return effectsCopy;
    }

    public boolean canClearAllEffect() {
        return clear;
    }

    public int getFireTime() {
        return fire;
    }

    public boolean canExtinguishing() {
        return extinguishing;
    }

    public FoodValue getFoodValue() {
        return foodValue;
    }

    public boolean canAlwaysDrink() {
        return !getFoodValue().isValid() || canAlwaysDrink;
    }

    public static FixedFluidEffect serialize(JsonObject json) {
        Fluid fluid = SerializeHelper.readFluid(json, "fluid");
        List<MobEffectInstance> effects = SerializeHelper.readEffects(json, "effects");
        boolean clear = GsonHelper.getAsBoolean(json, "clear", false);
        int fire = GsonHelper.getAsInt(json, "fire", 0);
        boolean extinguishing = GsonHelper.getAsBoolean(json, "extinguishing", false);
        FoodValue foodValue = SerializeHelper.readFoodValue(json, "food_value");
        boolean canAlwaysDrink = GsonHelper.getAsBoolean(json, "can_always_drink", false);
        return new FixedFluidEffect(fluid, effects, clear, fire, extinguishing, foodValue, canAlwaysDrink);
    }

    public static FixedFluidEffect getByFluid(Fluid fluid) {
        return ALL.stream()
                .filter(fixedFluidEffect -> fluid == fixedFluidEffect.getFluid())
                .findFirst()
                .orElse(EMPTY);
    }

    public static class Listener extends SimpleJsonResourceReloadListener {

        private static final Gson gson = new GsonBuilder().create();
        private static final String directory = "fixed_fluid_effects";

        public Listener() {
            super(gson, directory);
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
            map.forEach((location, jsonElement) -> {
                FixedFluidEffect fixedFluidEffect = FixedFluidEffect.serialize(jsonElement.getAsJsonObject());
                ALL.add(fixedFluidEffect);
            });
            IdyllicFoodDiary.DEBUG.getLogger().info("Successfully loaded {} fixed fluid effects.", ALL.size());
        }

    }

}
