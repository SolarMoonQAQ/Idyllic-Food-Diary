package cn.solarmoon.idyllic_food_diary.common.registry;


import cn.solarmoon.idyllic_food_diary.common.event.SnugEffectEvent;
import cn.solarmoon.idyllic_food_diary.common.event.SoupContainerEvent;
import cn.solarmoon.solarmoon_core.api.registry.base.BaseFMLEventRegistry;

public class IMCommonEvents extends BaseFMLEventRegistry {

    @Override
    public Side getSide() {
        return Side.COMMON;
    }

    @Override
    public void addRegistry() {
        add(new SnugEffectEvent());
        add(new SoupContainerEvent());
    }

}
