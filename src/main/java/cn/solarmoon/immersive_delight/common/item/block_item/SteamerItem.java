package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

public class SteamerItem extends BlockItem implements IContainerItem {

    public SteamerItem() {
        super(IMBlocks.STEAMER.get(), new Properties().stacksTo(1));
    }

}
