package cn.solarmoon.idyllic_food_diary.common.fluid;

import cn.solarmoon.idyllic_food_diary.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.idyllic_food_diary.common.registry.IMFluids;

/**
 * 绿茶
 */
public class GreenTeaFluid extends AbstractTeaFluid {

    public GreenTeaFluid() {
        super(IMFluids.GREEN_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
