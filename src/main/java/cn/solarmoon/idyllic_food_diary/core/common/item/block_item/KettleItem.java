package cn.solarmoon.idyllic_food_diary.core.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.api.common.item.AbstractKettleItem;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;

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
