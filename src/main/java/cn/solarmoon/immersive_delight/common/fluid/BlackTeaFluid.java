package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.api.common.fluid.BaseFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static cn.solarmoon.immersive_delight.common.registry.IMFluids.BlackTea.*;

/**
 * 红茶
 */
public class BlackTeaFluid {

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

    public static class Bucket extends BaseFluid.Bucket {
        public Bucket() {
            super(FLUID_STILL);
        }
    }

    private static ForgeFlowingFluid.Properties makeProperties() {
        return new ForgeFlowingFluid.Properties(FLUID_TYPE, FLUID_STILL, FLUID_FLOWING)
                .bucket(BUCKET)
                .block(FLUID_BLOCK);
    }

}
