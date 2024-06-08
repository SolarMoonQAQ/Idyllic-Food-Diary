package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.IItemRendererProvider;
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
