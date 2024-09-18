package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidType.class)
public abstract class FluidTypeMixin {

    @Shadow public abstract Component getDescription();

    @Inject(remap = false, method = "getDescription(Lnet/neoforged/neoforge/fluids/FluidStack;)Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    public void getDisplayName(FluidStack fluidStack, CallbackInfoReturnable<Component> cir) {
        Component tempName = fluidStack.getOrDefault(IFDDataComponents.getTEMP(), Temp.COMMON).getPrefix();
        cir.setReturnValue(tempName.copy().append(getDescription()));
    }

}
