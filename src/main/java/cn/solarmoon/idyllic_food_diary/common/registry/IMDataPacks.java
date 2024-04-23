package cn.solarmoon.idyllic_food_diary.common.registry;

import cn.solarmoon.idyllic_food_diary.data.fluid_effects.FluidEffectsBuilder;
import cn.solarmoon.solarmoon_core.api.registry.base.BaseDataPackRegistry;

public class IMDataPacks extends BaseDataPackRegistry {

    @Override
    public void addRegistry() {
        add(new FluidEffectsBuilder());
    }

}
