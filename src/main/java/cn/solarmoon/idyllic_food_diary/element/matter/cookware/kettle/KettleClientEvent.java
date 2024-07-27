package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class KettleClientEvent {

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        if (FMLEnvironment.dist.isClient()) {
            Player player = event.getEntity();
            ItemStack heldStack = event.getItemStack();
            InteractionHand hand = event.getHand();
            if (player.isCrouching() && hand == InteractionHand.MAIN_HAND && heldStack.getItem() instanceof AbstractKettleItem) {
                DumpVolumeScreen.open(heldStack);
                event.setCanceled(true);
            }
        }
    }

}
