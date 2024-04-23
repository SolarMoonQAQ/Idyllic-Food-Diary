package cn.solarmoon.idyllic_food_diary.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractKettleItem;

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
