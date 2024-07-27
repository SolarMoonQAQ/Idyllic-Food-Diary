package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class LongPlateItem extends AbstractContainerItem {

    public LongPlateItem(Block block) {
        super(block);
    }

    @Override
    public Supplier<BaseItemRenderer> getItemRenderer() {
        return LongPlateItemRenderer::new;
    }

}
