package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.item.base.AbstractKettleItem;

/**
 * 水壶item
 */
public class KettleItem extends AbstractKettleItem {

    public KettleItem() {
        super(IMBlocks.KETTLE.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

}
