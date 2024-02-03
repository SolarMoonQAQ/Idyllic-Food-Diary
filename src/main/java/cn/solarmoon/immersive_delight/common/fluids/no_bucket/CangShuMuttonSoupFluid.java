package cn.solarmoon.immersive_delight.common.fluids.no_bucket;

import cn.solarmoon.immersive_delight.api.common.fluid.BaseFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static cn.solarmoon.immersive_delight.common.registry.IMFluids.CangShuMuttonSoup.*;

public class CangShuMuttonSoupFluid {

    public static class FluidBlock extends BaseFluid.FluidBlock {
        public FluidBlock() {
            super(FLUID_STILL);
        }
    }

    public static class Flowing extends BaseFluid.Flowing {
        public Flowing() {
            super(makeProperties());
        }
    }

    public static class Source extends BaseFluid.Source {
        public Source() {
            super(makeProperties());
        }
    }

    private static ForgeFlowingFluid.Properties makeProperties() {
        return new ForgeFlowingFluid.Properties(FLUID_TYPE, FLUID_STILL, FLUID_FLOWING)
                .block(FLUID_BLOCK);
    }

}
