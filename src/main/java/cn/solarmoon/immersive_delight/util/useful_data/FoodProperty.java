package cn.solarmoon.immersive_delight.util.useful_data;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodProperty {

    public static FoodProperties PRIMARY_PRODUCT = new FoodProperties.Builder()
            .nutrition(1).saturationMod(0.5f).build();

    public static FoodProperties PRIMARY_HUNGER_PRODUCT = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.3F).effect(() ->
                    new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
            .build();

}
