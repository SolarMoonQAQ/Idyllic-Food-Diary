package cn.solarmoon.idyllic_food_diary.core.compat.patchouli.common.registry;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.compat.patchouli.common.item.FarmersDiaryItem;
import cn.solarmoon.solarmoon_core.api.common.registry.ItemEntry;

public class PItems {

    public static final ItemEntry<FarmersDiaryItem> FARMERS_DIARY = IdyllicFoodDiary.REGISTRY.item()
            .id("farmers_diary")
            .bound(FarmersDiaryItem::new)
            .build();

    public static void register() {}

}
