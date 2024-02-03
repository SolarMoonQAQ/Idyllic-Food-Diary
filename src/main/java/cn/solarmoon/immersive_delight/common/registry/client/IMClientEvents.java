package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.BaseEventRegistry;
import cn.solarmoon.immersive_delight.client.event.DrinkingClientEvent;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.client.event.TooltipGatherClientEvent;


public class IMClientEvents extends BaseEventRegistry {

    @Override
    public void addRegistry() {
        events.add(new RollingPinClientEvent());
        events.add(new TooltipGatherClientEvent());
        events.add(new DrinkingClientEvent());
    }

}
