package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.client.Item_renderer.GrillItemRenderer;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.IItemInventoryRendererProvider;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class GrillItem extends BlockItem implements IContainerItem, IItemInventoryRendererProvider {

    public GrillItem() {
        super(IMBlocks.GRILL.get(), new Properties().stacksTo(1));
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return GrillItemRenderer::new;
    }

}
