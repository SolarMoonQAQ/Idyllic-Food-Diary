package cn.solarmoon.idyllic_food_diary.compat.farmersdelight;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.util.useful_data.FoodProperty;
import cn.solarmoon.solarmoon_core.api.entry.common.ItemEntry;
import cn.solarmoon.solarmoon_core.api.item_base.SimpleFoodBlockItem;

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
