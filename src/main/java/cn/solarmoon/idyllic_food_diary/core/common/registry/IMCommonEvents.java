package cn.solarmoon.idyllic_food_diary.core.common.registry;


import cn.solarmoon.idyllic_food_diary.core.common.event.*;
import cn.solarmoon.solarmoon_core.api.common.registry.BaseCommonEventRegistry;

public class IMCommonEvents extends BaseCommonEventRegistry {

    @Override
    public void addRegistry() {
        add(new SnugEffectEvent());
        add(new SoupContainerEvent());
        add(new MakeSpiceJarFacilitateEvent());
        add(new BlockEntityDataHolderEvent());
        add(new BasicBlockEntityTickEvent());
    }

}
