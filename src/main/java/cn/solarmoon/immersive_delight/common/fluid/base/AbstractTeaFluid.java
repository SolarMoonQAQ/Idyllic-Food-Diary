package cn.solarmoon.immersive_delight.common.fluid.base;

import cn.solarmoon.immersive_delight.common.registry.IMEffects;
import cn.solarmoon.solarmoon_core.common.fluid.BaseFluid;
import cn.solarmoon.solarmoon_core.common.fluid.SimpleFluid;
import cn.solarmoon.solarmoon_core.registry.object.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractTeaFluid extends SimpleFluid {

    private final FluidEntry fluidEntry;

    public AbstractTeaFluid(FluidEntry fluidEntry) {
        super(fluidEntry);
        this.fluidEntry = fluidEntry;
    }

    public class FluidBlock extends BaseFluid.FluidBlock {

        public FluidBlock() {
            super(fluidEntry.getStillObject());
        }

        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            super.entityInside(state, level, pos, entity);
            //效果
            if(entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(IMEffects.TEA_AROMA.get(), 200, 0));
            }
        }

    }

}
