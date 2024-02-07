package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.core.BaseFMLEventRegistry;
import cn.solarmoon.immersive_delight.client.event.DrinkingClientEvent;
import cn.solarmoon.immersive_delight.client.event.RollingPinClientEvent;
import cn.solarmoon.immersive_delight.client.event.TooltipGatherClientEvent;


public class IMClientEvents extends BaseFMLEventRegistry {

    public IMClientEvents() {
        super(Side.CLIENT);
    }

    @Override
    public void addRegistry() {
        add(new RollingPinClientEvent());
        add(new TooltipGatherClientEvent());
        add(new DrinkingClientEvent());
    }

}
