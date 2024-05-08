package cn.solarmoon.idyllic_food_diary.core.common.fluid;

import cn.solarmoon.idyllic_food_diary.api.common.fluid.AbstractTeaFluid;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMFluids;

/**
 * 绿茶
 */
public class GreenTeaFluid extends AbstractTeaFluid {

    public GreenTeaFluid() {
        super(IMFluids.GREEN_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
