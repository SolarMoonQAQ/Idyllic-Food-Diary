package cn.solarmoon.idyllic_food_diary.core.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.core.client.Item_renderer.SteamerItemRenderer;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
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
