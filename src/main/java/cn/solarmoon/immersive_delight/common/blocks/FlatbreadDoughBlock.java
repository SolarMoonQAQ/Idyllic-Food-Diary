package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.common.blocks.abstract_blocks.LongPressEatBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class FlatbreadDoughBlock extends LongPressEatBlock {

    public FlatbreadDoughBlock() {
        super(Block.Properties.copy(Blocks.CAKE).destroyTime(1f));
    }

}
