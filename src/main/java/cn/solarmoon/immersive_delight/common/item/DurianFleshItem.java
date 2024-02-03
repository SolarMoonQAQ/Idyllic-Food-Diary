package cn.solarmoon.immersive_delight.common.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

/**
 * 榴莲肉
 */
public class DurianFleshItem extends Item {
    public DurianFleshItem() {
        super(new Properties()
                .food(new FoodProperties.Builder()
                .nutrition(4).saturationMod(0.5f).build()));
    }
}
