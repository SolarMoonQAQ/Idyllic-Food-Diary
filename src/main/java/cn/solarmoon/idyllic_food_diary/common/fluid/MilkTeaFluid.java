package cn.solarmoon.idyllic_food_diary.common.fluid;

import cn.solarmoon.idyllic_food_diary.common.fluid.base.AbstractTeaFluid;
import cn.solarmoon.idyllic_food_diary.common.registry.IMFluids;

public class MilkTeaFluid extends AbstractTeaFluid {

    public MilkTeaFluid() {
        super(IMFluids.MILK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
