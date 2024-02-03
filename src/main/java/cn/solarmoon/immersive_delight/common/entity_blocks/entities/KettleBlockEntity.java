package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.common.registry.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends BaseTankBlockEntity {

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.KETTLE_ENTITY.get(), pos, state, 1000);
    }

}
