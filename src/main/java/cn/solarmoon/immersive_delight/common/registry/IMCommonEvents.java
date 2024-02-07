package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.api.registry.core.BaseFMLEventRegistry;
import cn.solarmoon.immersive_delight.common.event.CleaverEvent;
import cn.solarmoon.immersive_delight.common.event.SnugEffectEvent;
import cn.solarmoon.immersive_delight.common.event.SoupContainerEvent;

public class IMCommonEvents extends BaseFMLEventRegistry {

    public IMCommonEvents() {
        super(Side.COMMON);
    }

    @Override
    public void addRegistry() {
        add(new SnugEffectEvent());
        add(new CleaverEvent());
        add(new SoupContainerEvent());
    }

}
