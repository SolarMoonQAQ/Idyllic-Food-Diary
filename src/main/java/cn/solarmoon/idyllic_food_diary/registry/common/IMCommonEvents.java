package cn.solarmoon.idyllic_food_diary.registry.common;


import cn.solarmoon.idyllic_food_diary.element.effect.SnugEffectEvent;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar.MakeSpiceJarFacilitateEvent;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.TransformWokPosEvent;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.BasicBlockEntityTickEvent;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.BlockEntityDataHolderEvent;
import cn.solarmoon.solarmoon_core.api.entry.common.BaseCommonEventRegistry;

public class IMCommonEvents extends BaseCommonEventRegistry {

    @Override
    public void addRegistry() {
        add(new SnugEffectEvent());
        add(new MakeSpiceJarFacilitateEvent());
        add(new BlockEntityDataHolderEvent());
        add(new BasicBlockEntityTickEvent());
        add(new TransformWokPosEvent());
    }

}
