package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.checkerframework.common.value.qual.MinLen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated
@Mixin(TileTank.class)
public abstract class FluidTankMixin extends FluidTank {

    public FluidTankMixin(int capacity) {
        super(capacity);
    }

    @Shadow public abstract BlockEntity getBlockEntity();

    @Inject(remap = false, method = "onContentsChanged", at = @At("RETURN"))
    public void onFluidChanged(CallbackInfo ci) {
        AnimHelper.Fluid.startFluidAnim(getBlockEntity(), getFluid());
    }

}
