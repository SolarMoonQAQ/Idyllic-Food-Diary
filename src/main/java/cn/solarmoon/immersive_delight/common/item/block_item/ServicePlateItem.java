package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.client.Item_renderer.PlateItemRenderer;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.client.ItemRenderer.IItemInventoryRendererProvider;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class ServicePlateItem extends BlockItem implements IContainerItem, IItemInventoryRendererProvider {

    public ServicePlateItem() {
        super(IMBlocks.SERVICE_PLATE.get(), new Properties());
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return PlateItemRenderer::new;
    }

}
