package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.AbstractKettleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FluidUtil.class)
public class FluidUtilMixin {

    @ModifyVariable(remap = false, method = "tryEmptyContainer", at = @At("HEAD"), index = 2, argsOnly = true)
    private static int modifyMaxAmount(int maxAmount, ItemStack container) {
        var opi = AbstractKettleItem.getDrainAmount(container);
        return opi.orElse(maxAmount);
    }

}
