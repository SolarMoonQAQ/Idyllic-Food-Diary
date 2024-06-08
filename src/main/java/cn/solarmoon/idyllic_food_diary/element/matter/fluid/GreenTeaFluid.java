package cn.solarmoon.idyllic_food_diary.element.matter.fluid;

import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;

/**
 * 绿茶
 */
public class GreenTeaFluid extends AbstractTeaFluid {

    public GreenTeaFluid() {
        super(IMFluids.GREEN_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
