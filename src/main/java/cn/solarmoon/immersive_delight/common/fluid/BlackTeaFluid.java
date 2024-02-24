package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import cn.solarmoon.solarmoon_core.common.fluid.SimpleFluid;

/**
 * 红茶
 */
public class BlackTeaFluid extends AbstractTeaFluid {

    public BlackTeaFluid() {
        super(IMFluids.BLACK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
