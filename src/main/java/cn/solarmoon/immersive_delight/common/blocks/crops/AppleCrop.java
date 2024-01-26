package cn.solarmoon.immersive_delight.common.blocks.crops;

import cn.solarmoon.immersive_delight.api.common.block.crop.BaseFruitCrop;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class AppleCrop extends BaseFruitCrop {

    public AppleCrop() {}

    @Override
    public Item getHarvestItem() {
        return Items.APPLE;
    }

}
