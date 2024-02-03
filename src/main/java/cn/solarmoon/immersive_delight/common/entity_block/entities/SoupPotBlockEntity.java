package cn.solarmoon.immersive_delight.common.entity_block.entities;

import cn.solarmoon.immersive_delight.common.registry.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupPotBlockEntity extends BaseTCBlockEntity {

    public SoupPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.SOUP_POT_ENTITY.get(), pos, state, 2000, 9, 1);
    }

}
