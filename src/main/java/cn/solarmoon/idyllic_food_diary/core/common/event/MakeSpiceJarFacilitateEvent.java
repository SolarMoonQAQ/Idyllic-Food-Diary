package cn.solarmoon.idyllic_food_diary.core.common.event;

import cn.solarmoon.idyllic_food_diary.api.common.item.AbstractSpiceJarItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 防止放调料的过程调用了方块的交互导致没放调料
 */
public class MakeSpiceJarFacilitateEvent {

    @SubscribeEvent
    public void onPlayerUseOn(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ItemStack heldItem = event.getItemStack();
        BlockPos pos = event.getPos();
        BlockHitResult hit = event.getHitVec();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        if (heldItem.getItem() instanceof AbstractSpiceJarItem && AbstractSpiceJarItem.hasEnoughSpice(heldItem)) {
            event.setUseBlock(Event.Result.DENY);
        }
    }

}
