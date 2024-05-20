package cn.solarmoon.idyllic_food_diary.core.client.registry;

import cn.solarmoon.idyllic_food_diary.core.client.event.CleaverClientEvent;
import cn.solarmoon.idyllic_food_diary.core.client.event.DrinkingClientEvent;
import cn.solarmoon.idyllic_food_diary.core.client.event.RollingPinClientEvent;
import cn.solarmoon.idyllic_food_diary.core.client.event.TooltipEvent;
import cn.solarmoon.solarmoon_core.api.client.registry.BaseClientEventRegistry;


public class IMClientEvents extends BaseClientEventRegistry {

    @Override
    public void addRegistry() {
        add(new DrinkingClientEvent());
        add(new RollingPinClientEvent());
        add(new CleaverClientEvent());
        add(new TooltipEvent());
    }

}
