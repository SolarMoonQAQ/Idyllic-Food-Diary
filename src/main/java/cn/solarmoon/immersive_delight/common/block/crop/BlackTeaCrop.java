package cn.solarmoon.immersive_delight.common.block.crop;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.api.common.block.crop.BaseTeaCrop;
import net.minecraft.world.item.Item;

public class BlackTeaCrop extends BaseTeaCrop {

    public BlackTeaCrop() {

    }

    /**
     * 产物为红茶叶
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.BLACK_TEA_LEAVES.get();
    }

}
