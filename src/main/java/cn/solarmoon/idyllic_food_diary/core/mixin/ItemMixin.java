package cn.solarmoon.idyllic_food_diary.core.mixin;

import cn.solarmoon.idyllic_food_diary.api.common.ability.SpittableItem;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract SoundEvent getDrinkingSound();

    private final Item item = (Item) (Object) this;

    /**
     * 苹果吐子儿
     */
    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    public void appleSpit(ItemStack stack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        for (SpittableItem spit : SpittableItem.ALL) {
            if (entity instanceof Player player) {
                spit.doSpit(player, stack);
            }
        }
    }

    /**
     * 改变原版汤的进食音效为喝
     */
    @Inject(method = "getEatingSound", at = @At("HEAD"), cancellable = true)
    public void setDrinkSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (item.equals(Items.BEETROOT_SOUP) || item.equals(Items.MUSHROOM_STEW)) {
            cir.setReturnValue(getDrinkingSound());
        }
    }

}
