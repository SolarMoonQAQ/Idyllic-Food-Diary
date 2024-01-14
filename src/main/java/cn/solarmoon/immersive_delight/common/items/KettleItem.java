package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.item.AbstractKettleItem;
import cn.solarmoon.immersive_delight.init.Config;

/**
 * 水壶item
 */
public class KettleItem extends AbstractKettleItem {

    public KettleItem() {
        super(IMEntityBlocks.KETTLE.get(), new Properties()

        );
    }

    @Override
    public int getMaxCapacity() {
        return Config.maxKettleCapacity.get();
    }

}
