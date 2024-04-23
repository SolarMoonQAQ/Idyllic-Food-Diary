package cn.solarmoon.idyllic_food_diary.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.client.Item_renderer.CuttingBoardItemRenderer;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class CuttingBoardItem extends BlockItem implements IContainerItem, IItemRendererProvider {

    public CuttingBoardItem() {
        super(IMBlocks.CUTTING_BOARD.get(), new Properties().stacksTo(1));
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return CuttingBoardItemRenderer::new;
    }
}
