package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.data.TeaIngredientsBuilder;
import cn.solarmoon.solarmoon_core.api.entry.common.BaseDataPackRegistry;

public class IMDataPacks extends BaseDataPackRegistry {

    @Override
    public void addRegistry() {
        add(new TeaIngredientsBuilder());
    }

}
