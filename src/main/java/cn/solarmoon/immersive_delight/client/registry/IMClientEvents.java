package cn.solarmoon.immersive_delight.client.registry;

import cn.solarmoon.immersive_delight.client.event.CleaverClientEvent;
import cn.solarmoon.immersive_delight.client.event.DrinkingClientEvent;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.solarmoon_core.registry.base.BaseFMLEventRegistry;


public class IMClientEvents extends BaseFMLEventRegistry {

    public IMClientEvents() {
        super(Side.CLIENT);
    }

    @Override
    public void addRegistry() {
        add(new DrinkingClientEvent());
        add(new RollingPinClientEvent());
        add(new CleaverClientEvent());
    }

}
