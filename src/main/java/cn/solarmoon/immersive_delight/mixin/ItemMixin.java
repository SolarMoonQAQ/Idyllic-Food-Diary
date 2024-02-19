package cn.solarmoon.immersive_delight.mixin;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    /**
     * 苹果吐子儿
     */
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void appleSpit(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.is(Items.APPLE) && entity instanceof Player player) {
            FarmerUtil.spit(IMItems.APPLE_CORE.get(), 1, player);
        }
    }



}
