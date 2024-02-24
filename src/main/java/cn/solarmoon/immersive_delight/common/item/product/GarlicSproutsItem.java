package cn.solarmoon.immersive_delight.common.item.product;

import cn.solarmoon.immersive_delight.util.useful_data.FoodProperty;
import net.minecraft.world.item.Item;

public class GarlicSproutsItem extends Item {

    public GarlicSproutsItem() {
        super(new Properties().food(FoodProperty.PRIMARY_PRODUCT));
    }

}
