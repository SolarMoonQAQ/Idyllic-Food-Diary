package cn.solarmoon.idyllic_food_diary.element.matter.crop;

import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseHangingBushCropBlock;
import cn.solarmoon.solarmoon_core.api.util.device.BlockMatcher;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class AppleCropBlock extends BaseHangingBushCropBlock {

    public AppleCropBlock() {}

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public BlockMatcher canSurviveBlock() {
        return BlockMatcher.of(Blocks.OAK_LEAVES);
    }

    @Override
    public Item getHarvestItem() {
        return Items.APPLE;
    }

}
