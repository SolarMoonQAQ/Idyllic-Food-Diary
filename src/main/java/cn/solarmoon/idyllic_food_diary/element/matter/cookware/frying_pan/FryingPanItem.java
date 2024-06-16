package cn.solarmoon.idyllic_food_diary.element.matter.cookware.frying_pan;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.item_util.IContainerItem;
import cn.solarmoon.solarmoon_core.api.item_util.ITankItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.IItemRendererProvider;
import net.minecraft.world.item.BlockItem;

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
