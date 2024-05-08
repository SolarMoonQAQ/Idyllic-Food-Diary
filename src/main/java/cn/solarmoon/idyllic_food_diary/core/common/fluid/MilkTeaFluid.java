package cn.solarmoon.idyllic_food_diary.core.common.fluid;

import cn.solarmoon.idyllic_food_diary.api.common.fluid.AbstractTeaFluid;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMFluids;

public class MilkTeaFluid extends AbstractTeaFluid {

    public MilkTeaFluid() {
        super(IMFluids.MILK_TEA);
    }

    public class FluidBlock extends AbstractTeaFluid.FluidBlock {}

}
