package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.matter.cleaver.CleaverClientEvent;
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinClientEvent;
import cn.solarmoon.idyllic_food_diary.feature.logic.water_pouring.DrinkingClientEvent;
import cn.solarmoon.idyllic_food_diary.feature.visual.spice_tooltip.TooltipEvent;
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
