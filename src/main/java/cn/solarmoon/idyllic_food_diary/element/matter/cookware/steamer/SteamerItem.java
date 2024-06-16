package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.item_util.IContainerItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.IItemRendererProvider;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class SteamerItem extends BlockItem implements IContainerItem, IItemRendererProvider {

    public SteamerItem() {
        super(IMBlocks.STEAMER.get(), new Properties().stacksTo(1));
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return SteamerItemRenderer::new;
    }

}
