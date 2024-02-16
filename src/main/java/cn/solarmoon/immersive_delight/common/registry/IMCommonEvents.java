package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.common.event.SnugEffectEvent;
import cn.solarmoon.immersive_delight.common.event.SoupContainerEvent;
import cn.solarmoon.solarmoon_core.registry.base.BaseFMLEventRegistry;

public class IMCommonEvents extends BaseFMLEventRegistry {

    public IMCommonEvents() {
        super(Side.COMMON);
    }

    @Override
    public void addRegistry() {
        add(new SnugEffectEvent());
        add(new SoupContainerEvent());
    }

}
