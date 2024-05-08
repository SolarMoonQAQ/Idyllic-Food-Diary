package cn.solarmoon.idyllic_food_diary.core.common.registry;

import cn.solarmoon.idyllic_food_diary.core.data.fluid_effects.FluidEffectsBuilder;
import cn.solarmoon.solarmoon_core.api.common.registry.BaseDataPackRegistry;

public class IMDataPacks extends BaseDataPackRegistry {

    @Override
    public void addRegistry() {
        add(new FluidEffectsBuilder());
    }

}
