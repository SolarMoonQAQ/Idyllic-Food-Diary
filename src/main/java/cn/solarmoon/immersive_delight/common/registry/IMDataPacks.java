package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.api.registry.core.BaseDataPackRegistry;
import cn.solarmoon.immersive_delight.data.fluid_effects.FluidEffectsBuilder;
import cn.solarmoon.immersive_delight.data.fluid_foods.FluidFoodsBuilder;

public class IMDataPacks extends BaseDataPackRegistry {

    @Override
    public void addRegistry() {
        add(new FluidEffectsBuilder());
        add(new FluidFoodsBuilder());
    }

}
