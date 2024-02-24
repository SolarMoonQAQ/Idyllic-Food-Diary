package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import cn.solarmoon.solarmoon_core.common.fluid.SimpleFluid;

public class MilkTeaFluid extends AbstractTeaFluid {

    public MilkTeaFluid() {
        super(IMFluids.MILK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
