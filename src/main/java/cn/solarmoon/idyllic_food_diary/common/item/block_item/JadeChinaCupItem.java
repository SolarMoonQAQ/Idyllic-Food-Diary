package cn.solarmoon.idyllic_food_diary.common.item.block_item;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractCupItem;

public class JadeChinaCupItem extends AbstractCupItem {

    public JadeChinaCupItem() {
        super(IMBlocks.JADE_CHINA_CUP.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
