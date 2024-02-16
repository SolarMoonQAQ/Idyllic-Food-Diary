package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

public class SoupPotItem extends BlockItem implements ITankItem, IContainerItem {

    public SoupPotItem() {
        super(IMBlocks.SOUP_POT.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 2000;
    }

}
