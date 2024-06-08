package cn.solarmoon.idyllic_food_diary.element.matter.fluid;

import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;

public class MilkTeaFluid extends AbstractTeaFluid {

    public MilkTeaFluid() {
        super(IMFluids.MILK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
