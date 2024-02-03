package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.BaseEventRegistry;
import cn.solarmoon.immersive_delight.common.events.CleaverEvent;
import cn.solarmoon.immersive_delight.common.events.RollingPinEvent;
import cn.solarmoon.immersive_delight.common.events.SnugEffectEvent;
import cn.solarmoon.immersive_delight.common.events.SoupContainerEvent;

public class IMCommonEvents extends BaseEventRegistry {

    @Override
    public void addRegistry() {
        events.add(new RollingPinEvent());
        events.add(new SnugEffectEvent());
        events.add(new CleaverEvent());
        events.add(new SoupContainerEvent());
    }

}
