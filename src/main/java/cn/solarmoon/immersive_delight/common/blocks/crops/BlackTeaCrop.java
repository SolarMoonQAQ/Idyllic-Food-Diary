package cn.solarmoon.immersive_delight.common.blocks.crops;

import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.api.common.block.crop.BaseBashTeaCrop;
import net.minecraft.world.item.Item;

public class BlackTeaCrop extends BaseBashTeaCrop {

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
