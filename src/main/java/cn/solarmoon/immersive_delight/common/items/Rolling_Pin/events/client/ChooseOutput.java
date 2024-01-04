package cn.solarmoon.immersive_delight.common.items.Rolling_Pin.events.client;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static cn.solarmoon.immersive_delight.util.Constants.mc;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HitResultRecipeCheck.hitResultRecipeCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.methods.client.HoldRollingCheck.holdRollingCheck;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer.DrawItem.scale;
import static cn.solarmoon.immersive_delight.common.items.Rolling_Pin.renderer.DrawLittleItem.scaleB;


public class ChooseOutput {
    public static List<ItemStack> possibleOutputs;
    public static List<ItemStack> actualResults;

    //滚动鼠标动态更新数组物品指定行
    public static int currentRecipeIndex = 0;

    public static boolean upRoll = false;
    public static boolean downRoll = false;

    @SubscribeEvent
    public void chooseOutPut(InputEvent.MouseScrollingEvent event) {
        if (mc.player != null && possibleOutputs != null && !possibleOutputs.isEmpty() && holdRollingCheck() && hitResultRecipeCheck() && mc.player.isCrouching()) {
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
}
