package cn.solarmoon.immersive_delight.common.fluids;

import cn.solarmoon.immersive_delight.api.common.fluid.BaseHotFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static cn.solarmoon.immersive_delight.common.registry.IMFluids.HotWater.*;

/**
 * 热水，站入其中受到伤害
 * 有4级以下的火焰保护不受伤害
 * 有防火药水或四级以上的火焰保护不受伤害且获得增益
 */
public class HotWaterFluid {

    public static class FluidBlock extends BaseHotFluid.FluidBlock {
        public FluidBlock() {
            super(FLUID_STILL);
        }

    }

    public static class Flowing extends BaseHotFluid.Flowing {
        public Flowing() {
            super(makeProperties());
        }
    }

    public static class Source extends BaseHotFluid.Source {
        public Source() {
            super(makeProperties());
        }
    }

    public static class Bucket extends BaseHotFluid.Bucket {
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
