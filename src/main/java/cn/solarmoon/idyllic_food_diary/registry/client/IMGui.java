package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.feature.optional_gui.IOptionalRecipeItemGui;
import cn.solarmoon.solarmoon_core.api.entry.client.BaseGuiRegistry;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class IMGui extends BaseGuiRegistry {

    @Override
    public void registerGUIs(RegisterGuiOverlaysEvent event) {
        //擀面杖
        event.registerAbove(VanillaGuiOverlay.DEBUG_TEXT.id(), "optional_recipe_item_gui", (gui, guiGraphics, partialTick, width, height) -> {
            gui.setupOverlayRenderState(true, true);
            IOptionalRecipeItemGui.render(guiGraphics, "recipe_selection");
        });
    }

}
