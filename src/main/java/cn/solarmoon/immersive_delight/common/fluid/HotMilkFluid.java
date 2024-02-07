package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.common.fluid.core.AbstractHotFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static cn.solarmoon.immersive_delight.common.registry.IMFluids.HotMilk.*;

/**
 * 热牛奶，站入其中受到伤害
 * 有4级以下的火焰保护不受伤害
 * 有防火药水或四级以上的火焰保护不受伤害且获得增益
 */
public class HotMilkFluid {
    public static class FluidBlock extends AbstractHotFluid.FluidBlock {
        public FluidBlock() {
            super(FLUID_STILL);
        }

    }

    public static class Flowing extends AbstractHotFluid.Flowing {
        public Flowing() {
            super(makeProperties());
        }
    }

    public static class Source extends AbstractHotFluid.Source {
        public Source() {
            super(makeProperties());
        }

    }

    public static class Bucket extends AbstractHotFluid.Bucket {
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
