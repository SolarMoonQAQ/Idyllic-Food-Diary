package cn.solarmoon.immersive_delight.common.items;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.item.BaseTankItem;

public class SoupPotItem extends BaseTankItem {

    public SoupPotItem() {
        super(IMEntityBlocks.SOUP_POT.get(), new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 2000;
    }

}
