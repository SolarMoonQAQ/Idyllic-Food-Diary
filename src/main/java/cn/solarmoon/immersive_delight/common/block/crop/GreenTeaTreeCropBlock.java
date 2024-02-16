package cn.solarmoon.immersive_delight.common.block.crop;

import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.common.block.crop.BaseBushCropBlock;
import net.minecraft.world.item.Item;

public class GreenTeaTreeCropBlock extends BaseBushCropBlock {

    public GreenTeaTreeCropBlock() {

    }

    /**
     * 产物为绿茶叶
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.GREEN_TEA_LEAVES.get();
    }

}
