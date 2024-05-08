package cn.solarmoon.idyllic_food_diary.core.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.api.common.item.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;

public class JadeChinaCupItem extends AbstractCupItem {

    public JadeChinaCupItem() {
        super(IMBlocks.JADE_CHINA_CUP.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
