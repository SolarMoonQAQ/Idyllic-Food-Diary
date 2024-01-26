package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.api.common.item.specific.AbstractBowlFoodItem;
import cn.solarmoon.immersive_delight.common.IMBlocks;
import net.minecraft.world.food.FoodProperties;

public class CangshuMuttonSoupItem extends AbstractBowlFoodItem {
    public CangshuMuttonSoupItem() {
        super("immersive_delight:cangshu_mutton_soup_fluid", IMBlocks.CANGSHU_MUTTON_SOUP.get(), new Properties().food(
                new FoodProperties.Builder()
                .nutrition(6).saturationMod(1.5F).build()
                )
        );
    }
}
