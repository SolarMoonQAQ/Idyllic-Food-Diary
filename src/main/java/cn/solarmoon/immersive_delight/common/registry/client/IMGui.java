package cn.solarmoon.immersive_delight.common.registry.client;

import cn.solarmoon.immersive_delight.api.registry.core.BaseGuiRegistry;
import cn.solarmoon.immersive_delight.client.gui.RollingPinGui;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class IMGui extends BaseGuiRegistry {

    @Override
    public void registerGUIs(RegisterGuiOverlaysEvent event) {
        //擀面杖
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "rolling_pin_gui", (gui, poseStack, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, true);
            new RollingPinGui(poseStack);
        });
    }

}
