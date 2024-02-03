package cn.solarmoon.immersive_delight.common.block.sapling;

import cn.solarmoon.immersive_delight.common.level.feature.AppleTreeFeature;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;

public class AppleSaplingBlock extends SaplingBlock {

    public AppleSaplingBlock() {
        super(new AppleTreeFeature.AppleTreeGrower(), Block.Properties.copy(Blocks.OAK_SAPLING));
    }

}
