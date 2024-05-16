package cn.solarmoon.idyllic_food_diary.core.mixin;

import cn.solarmoon.idyllic_food_diary.api.data.serializer.TeaIngredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidType.class)
public abstract class FluidTypeMixin {

    @Shadow public abstract Component getDescription();

    @Inject(remap = false, method = "getDescription(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    public void getDisplayName(FluidStack fluidStack, CallbackInfoReturnable<Component> cir) {
        if (!fluidStack.isEmpty()) {
            CompoundTag nameTag = fluidStack.getOrCreateTag().getCompound("CustomName");
            String side = nameTag.getString(TeaIngredient.Type.SIDE.toString());
            String add = nameTag.getString(TeaIngredient.Type.ADD.toString());
            Component sideC = side.isEmpty() ? Component.empty() : Component.translatable(side);
            Component addC = add.isEmpty() ? Component.empty() : Component.translatable(add);
            cir.setReturnValue(sideC.copy().append(addC).append(getDescription()));
        }
    }

}
