package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.items.abstract_items.BaseTankItem;
import cn.solarmoon.immersive_delight.init.Config;

public class JadeChinaCupItem extends BaseTankItem {
    public JadeChinaCupItem() {
        super(IMEntityBlocks.JADE_CHINA_CUP.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return Config.maxJadeChinaCupCapacity.get();
    }
}
