package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.common.item.IContainerItem;
import net.minecraft.world.item.BlockItem;

public class CuttingBoardItem extends BlockItem implements IContainerItem {

    public CuttingBoardItem() {
        super(IMBlocks.CUTTING_BOARD.get(), new Properties().stacksTo(1));
    }

}
