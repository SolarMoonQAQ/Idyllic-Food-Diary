package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FluidUtil.class)
public class FluidUtilMixin {

    @ModifyVariable(remap = false, method = "tryEmptyContainer", at = @At("HEAD"), index = 2, argsOnly = true)
    private static int modifyMaxAmount(int maxAmount, ItemStack container) {
        var opi = container.get(IFDDataComponents.getFLUID_INTERACT_VALUE());
        return opi == null ? maxAmount : opi;
    }

}
