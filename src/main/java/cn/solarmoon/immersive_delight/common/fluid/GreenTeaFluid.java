package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import cn.solarmoon.solarmoon_core.common.fluid.SimpleFluid;

/**
 * 绿茶
 */
public class GreenTeaFluid extends AbstractTeaFluid {

    public GreenTeaFluid() {
        super(IMFluids.GREEN_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
