package cn.solarmoon.idyllic_food_diary.core.mixin;

import cn.solarmoon.idyllic_food_diary.api.util.FluidTypeUtil;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.extensions.IForgeBoat;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 使船能在IM液体上行驶
 */
@Mixin(Boat.class)
public class BoatMixin implements IForgeBoat {

    //模组液体

    @Override
    public boolean canBoatInFluid(FluidState state)
    {
        return state.supportsBoating(((Boat) (Object) this))
                || FluidTypeUtil.IMFluidsMatch(state.getFluidType());
    }

    @Override
    public boolean canBoatInFluid(FluidType type)
    {
        return type.supportsBoating(((Boat) (Object) this))
                || FluidTypeUtil.IMFluidsMatch(type);
    }

}
