package cn.solarmoon.idyllic_food_diary.compat.appleskin;

import cn.solarmoon.solarmoon_core.api.client.registry.BaseClientEventRegistry;

public class ASClientEvents extends BaseClientEventRegistry {

    @Override
    public void addRegistry() {
        add(new ShowCupFoodValueEvent());
    }

}
