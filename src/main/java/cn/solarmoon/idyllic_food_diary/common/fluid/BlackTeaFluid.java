package cn.solarmoon.idyllic_food_diary.common.fluid;

import cn.solarmoon.idyllic_food_diary.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.idyllic_food_diary.common.registry.IMFluids;

/**
 * 红茶
 */
public class BlackTeaFluid extends AbstractTeaFluid {

    public BlackTeaFluid() {
        super(IMFluids.BLACK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
