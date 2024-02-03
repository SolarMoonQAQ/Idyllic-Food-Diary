package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.registry.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.item.specific.AbstractKettleItem;

/**
 * 水壶item
 */
public class KettleItem extends AbstractKettleItem {

    public KettleItem() {
        super(IMEntityBlocks.KETTLE.get(), 1000, new Properties()

        );
    }

}
