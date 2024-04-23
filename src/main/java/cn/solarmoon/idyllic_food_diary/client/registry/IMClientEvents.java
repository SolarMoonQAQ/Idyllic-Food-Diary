package cn.solarmoon.idyllic_food_diary.client.registry;

import cn.solarmoon.idyllic_food_diary.client.event.CleaverClientEvent;
import cn.solarmoon.idyllic_food_diary.client.event.DrinkingClientEvent;
import cn.solarmoon.idyllic_food_diary.client.event.RollingPinClientEvent;
import cn.solarmoon.solarmoon_core.api.registry.base.BaseFMLEventRegistry;


public class IMClientEvents extends BaseFMLEventRegistry {

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public void addRegistry() {
        add(new DrinkingClientEvent());
        add(new RollingPinClientEvent());
        add(new CleaverClientEvent());
    }

}
