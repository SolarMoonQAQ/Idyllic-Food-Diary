package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.IItemRendererProvider;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public class CookingPotItem extends BlockItem implements ITankItem, IContainerItem, IItemRendererProvider {

    public CookingPotItem() {
        super(IMBlocks.COOKING_POT.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return CookingPotItemRenderer::new;
    }

}
