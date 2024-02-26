package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.common.item.ITankItem;
import net.minecraft.world.item.BlockItem;

public class SteamerBaseItem extends BlockItem implements ITankItem {

    public SteamerBaseItem() {
        super(IMBlocks.STEAMER_BASE.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 2000;
    }

}
