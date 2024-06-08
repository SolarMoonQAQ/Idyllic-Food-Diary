package cn.solarmoon.idyllic_food_diary.element.matter.fluid;

import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;

/**
 * 红茶
 */
public class BlackTeaFluid extends AbstractTeaFluid {

    public BlackTeaFluid() {
        super(IMFluids.BLACK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
