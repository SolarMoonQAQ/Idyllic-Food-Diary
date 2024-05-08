package cn.solarmoon.idyllic_food_diary.core.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.core.client.Item_renderer.SoupPotItemRenderer;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class SoupPotItem extends BlockItem implements ITankItem, IContainerItem, IItemRendererProvider {

    public SoupPotItem() {
        super(IMBlocks.SOUP_POT.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return SoupPotItemRenderer::new;
    }

}
