package cn.solarmoon.idyllic_food_diary.api.util.useful_data;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodProperty {

    public static FoodProperties PRIMARY_PRODUCT = new FoodProperties.Builder()
            .nutrition(1).saturationMod(0.5f).build();

    public static FoodProperties PRIMARY_PRODUCT_1 = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.5f).build();

    public static FoodProperties PRIMARY_PRODUCT_2 = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.3f).build();

    public static FoodProperties PRIMARY_HUNGER_PRODUCT = new FoodProperties.Builder()
            .nutrition(1).saturationMod(0.3F).effect(() ->
                    new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
            .build();

    public static FoodProperties PRIMARY_HUNGER_PRODUCT_1 = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.2F).effect(() ->
                    new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
            .build();

}
