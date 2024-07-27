package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class PlateItem extends AbstractContainerItem {

    public PlateItem(Block block) {
        super(block);
    }

    @Override
    public Supplier<BaseItemRenderer> getItemRenderer() {
        return PlateItemRenderer::new;
    }

}
