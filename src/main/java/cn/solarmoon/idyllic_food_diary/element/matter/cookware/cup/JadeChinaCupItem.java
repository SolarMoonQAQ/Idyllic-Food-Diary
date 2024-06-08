package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;

public class JadeChinaCupItem extends AbstractCupItem {

    public JadeChinaCupItem() {
        super(IMBlocks.JADE_CHINA_CUP.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

}
