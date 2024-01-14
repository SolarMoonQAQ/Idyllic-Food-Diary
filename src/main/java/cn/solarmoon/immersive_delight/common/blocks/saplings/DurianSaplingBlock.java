package cn.solarmoon.immersive_delight.common.blocks.saplings;

import cn.solarmoon.immersive_delight.common.level.feature.DurianTreeFeature;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;

public class DurianSaplingBlock extends SaplingBlock {
    public DurianSaplingBlock() {
        super(new DurianTreeFeature.DurianTreeGrower() , Block.Properties.copy(Blocks.OAK_SAPLING));
    }
}
