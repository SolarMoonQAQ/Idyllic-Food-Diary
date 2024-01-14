package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseContainerTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupPotBlockEntity extends BaseContainerTankBlockEntity {

    public SoupPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.SOUP_POT_ENTITY.get(), pos, state, 2000, 9, 1);
    }

}
