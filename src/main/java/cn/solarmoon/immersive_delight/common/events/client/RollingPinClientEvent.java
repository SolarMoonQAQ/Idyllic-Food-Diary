package cn.solarmoon.immersive_delight.common.events.client;

import cn.solarmoon.immersive_delight.common.events.RollingPinEvent;
import cn.solarmoon.immersive_delight.common.items.RollingPinItem;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawItem.scale;
import static cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawLittleItem.scaleB;
import static cn.solarmoon.immersive_delight.client.particles.vanilla.Sweep.sweep;
import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class RollingPinClientEvent {

    public static List<ItemStack> possibleOutputs;
    public static List<ItemStack> actualResults;

    //滚动鼠标动态更新数组物品指定行
    public static int currentRecipeIndex = 0;

    public static boolean upRoll = false;
    public static boolean downRoll = false;

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        if (mc.player != null && possibleOutputs != null && !possibleOutputs.isEmpty() && RollingPinItem.holdRollingCheck() && RollingPinItem.hitResultRecipeCheck() && mc.player.isCrouching()) {
            // 根据鼠标滚动的方向更新索引
            if (event.getScrollDelta() > 0) {
                currentRecipeIndex++;
                if (currentRecipeIndex >= possibleOutputs.size()) {
                    currentRecipeIndex = 0;
                }
                upRoll = true;
                downRoll = false;
                scale = 1.0F;
                scaleB = 1.6f;
            } else if (event.getScrollDelta() < 0) {
                currentRecipeIndex--;
                if (currentRecipeIndex < 0) {
                    currentRecipeIndex = possibleOutputs.size() - 1;
                }
                downRoll = true;
                upRoll = false;
                scale = 1.0F;
                scaleB = 1.6f;
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void updateMatchRecipe(TickEvent.ClientTickEvent event) {
        if (mc.player != null && RollingPinItem.holdRollingCheck() && RollingPinItem.hitResultRecipeCheck() && mc.player.isCrouching()) {
            Block block = null;
            if (mc.level != null) {
                if (mc.hitResult != null) {
                    block = mc.level.getBlockState(((BlockHitResult)mc.hitResult).getBlockPos()).getBlock();
                }
            }
            RollingPinItem.updatePossibleOutputs(block);
        }
    }

    //擀面风暴！
    @SubscribeEvent
    public void sweepHarvest(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        if(!itemStack.is(ROLLING_PIN.get()) || !player.isCrouching()) return;
        RollingPinEvent.harvest(0, player, event.getLevel());
    }

}
