package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cleaver.CleaverClientEvent;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.rolling_pin.RollingPinClientEvent;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceTooltipEvent;
import cn.solarmoon.idyllic_food_diary.feature.water_pouring.DrinkingClientEvent;
import cn.solarmoon.solarmoon_core.api.entry.client.BaseClientEventRegistry;


public class IMClientEvents extends BaseClientEventRegistry {

    @Override
    public void addRegistry() {
        add(new DrinkingClientEvent());
        add(new RollingPinClientEvent());
        add(new CleaverClientEvent());
        add(new SpiceTooltipEvent());
    }

}
