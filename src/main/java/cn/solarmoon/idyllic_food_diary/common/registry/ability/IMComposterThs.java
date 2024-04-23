package cn.solarmoon.idyllic_food_diary.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.registry.base.BaseComposterRegistry;

public class IMComposterThs extends BaseComposterRegistry {
    @Override
    public void addRegistry() {
        put(IMItems.APPLE_CORE.get(), 0.3F);
    }
}
