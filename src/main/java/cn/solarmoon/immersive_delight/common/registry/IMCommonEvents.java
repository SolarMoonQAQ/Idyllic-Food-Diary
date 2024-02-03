package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.BaseEventRegistry;
import cn.solarmoon.immersive_delight.common.event.CleaverEvent;
import cn.solarmoon.immersive_delight.common.event.RollingPinEvent;
import cn.solarmoon.immersive_delight.common.event.SnugEffectEvent;
import cn.solarmoon.immersive_delight.common.event.SoupContainerEvent;

public class IMCommonEvents extends BaseEventRegistry {

    @Override
    public void addRegistry() {
        events.add(new RollingPinEvent());
        events.add(new SnugEffectEvent());
        events.add(new CleaverEvent());
        events.add(new SoupContainerEvent());
    }

}
