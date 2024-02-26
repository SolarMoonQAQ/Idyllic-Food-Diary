package cn.solarmoon.immersive_delight.common.item.food_block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;

public class SteamedBunItem extends BlockItem {

    public static final FoodProperties foodProperties = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.5F).build();

    public SteamedBunItem() {
        super(IMBlocks.STEAMED_BUN.get(), new Properties().food(foodProperties));
    }

}
