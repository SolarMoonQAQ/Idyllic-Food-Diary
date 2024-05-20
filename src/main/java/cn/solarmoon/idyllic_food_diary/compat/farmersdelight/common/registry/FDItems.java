package cn.solarmoon.idyllic_food_diary.compat.farmersdelight.common.registry;

import cn.solarmoon.idyllic_food_diary.api.util.useful_data.FoodProperty;
import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.common.item.simple.SimpleFoodBlockItem;
import cn.solarmoon.solarmoon_core.api.common.registry.ItemEntry;

public class FDItems {

    /**
     * 覆盖fd面团
     */
    public static final ItemEntry<SimpleFoodBlockItem> WHEAT_DOUGH = FarmersDelight.REGISTRY.item()
            .id("wheat_dough")
            .bound(() -> new SimpleFoodBlockItem(IMBlocks.WHEAT_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT))
            .build();

    public static void register() {}

}
