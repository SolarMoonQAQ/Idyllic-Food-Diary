package cn.solarmoon.idyllic_food_diary.element.matter.cookware.frying_pan;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class FryingPanItem extends BlockItem implements ITankItem, IContainerItem, IItemRendererProvider {

    public FryingPanItem() {
        super(IMBlocks.FRYING_PAN.get(), new Properties().stacksTo(1));
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return FryingPanItemRenderer::new;
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
