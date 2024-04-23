package cn.solarmoon.idyllic_food_diary.common.block.crop;

import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseHangingBushCropBlock;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class AppleCropBlock extends BaseHangingBushCropBlock {

    public AppleCropBlock() {}

    @Override
    public Block canSurviveBlock() {
        return Blocks.OAK_LEAVES;
    }

    @Override
    public TagKey<Block> canSurviveTag() {
        return null;
    }

    @Override
    public Item getHarvestItem() {
        return Items.APPLE;
    }

}
