package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawItem;
import cn.solarmoon.immersive_delight.client.gui.rolling_pin.DrawLittleItem;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMGui {

    //注册GUI
    @SubscribeEvent
    public static void registerGUI(RegisterGuiOverlaysEvent event) {
        //擀面杖
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "draw_item", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, true);
            DrawItem.drawItem(poseStack);
        });
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "draw_little_item", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, false);
            DrawLittleItem.drawLittleItem(poseStack);
        });
    }

}
