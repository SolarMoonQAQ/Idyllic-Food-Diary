package cn.solarmoon.immersive_delight.common.item.food_item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class BaseFoodItem extends Item {

    public BaseFoodItem(int nutrition, float saturation) {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public BaseFoodItem(FoodProperties foodProperties) {
        super(new Properties()
                .food(foodProperties));
    }

}
