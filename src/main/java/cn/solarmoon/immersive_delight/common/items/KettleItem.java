package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.common.items.abstract_items.WaterKettleItem;
import cn.solarmoon.immersive_delight.init.Config;

/**
 * 水壶item
 */
public class KettleItem extends WaterKettleItem {

    public KettleItem() {
        super(IMEntityBlocks.KETTLE.get(), new Properties()

        );
    }

    @Override
    public int getMaxCapacity() {
        return Config.maxKettleCapacity.get();
    }

}
