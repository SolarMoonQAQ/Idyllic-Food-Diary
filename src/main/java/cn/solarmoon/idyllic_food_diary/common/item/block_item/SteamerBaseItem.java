package cn.solarmoon.idyllic_food_diary.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.client.Item_renderer.SteamerBaseItemRenderer;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class SteamerBaseItem extends BlockItem implements ITankItem, IItemRendererProvider {

    public SteamerBaseItem() {
        super(IMBlocks.STEAMER_BASE.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return SteamerBaseItemRenderer::new;
    }
}
