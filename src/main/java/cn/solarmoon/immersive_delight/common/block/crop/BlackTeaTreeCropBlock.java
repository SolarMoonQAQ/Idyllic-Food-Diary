package cn.solarmoon.immersive_delight.common.block.crop;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.common.block.crop.BaseBushCropBlock;
import net.minecraft.world.item.Item;

public class BlackTeaTreeCropBlock extends BaseBushCropBlock {

    public BlackTeaTreeCropBlock() {

    }

    /**
     * 产物为红茶叶
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.BLACK_TEA_LEAVES.get();
    }

}
