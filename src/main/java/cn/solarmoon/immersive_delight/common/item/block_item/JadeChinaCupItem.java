package cn.solarmoon.immersive_delight.common.item.block_item;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.item.base.AbstractCupItem;

public class JadeChinaCupItem extends AbstractCupItem {

    public JadeChinaCupItem() {
        super(IMBlocks.JADE_CHINA_CUP.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
