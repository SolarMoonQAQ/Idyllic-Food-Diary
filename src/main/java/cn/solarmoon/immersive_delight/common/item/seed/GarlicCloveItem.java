package cn.solarmoon.immersive_delight.common.item.seed;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.util.useful_data.FoodProperty;
import net.minecraft.world.item.ItemNameBlockItem;

public class GarlicCloveItem extends ItemNameBlockItem {

    public GarlicCloveItem() {
        super(IMBlocks.GARLIC_CROP.get(), new Properties().food(FoodProperty.PRIMARY_PRODUCT));
    }

}
