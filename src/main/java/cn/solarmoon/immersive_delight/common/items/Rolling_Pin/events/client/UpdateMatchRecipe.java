package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.immersive_delight.util.Constants.mc;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HitResultRecipeCheck.hitResultRecipeCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HoldRollingCheck.holdRollingCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.UpdatePossibleOutputs.updatePossibleOutputs;


public class UpdateMatchRecipe {

    @SubscribeEvent
    public void updateMatchRecipe(TickEvent.ClientTickEvent event) {
        if (mc.player != null && holdRollingCheck() && hitResultRecipeCheck() && mc.player.isCrouching()) {
            Block block = null;
            if (mc.level != null) {
                if (mc.hitResult != null) {
                    block = mc.level.getBlockState(((BlockHitResult)mc.hitResult).getBlockPos()).getBlock();
                }
            }
            updatePossibleOutputs(block);
        }
    }

}
