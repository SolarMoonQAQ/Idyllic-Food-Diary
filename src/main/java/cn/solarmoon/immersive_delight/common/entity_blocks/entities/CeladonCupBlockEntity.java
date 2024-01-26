package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankContainerBlockEntity;
import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CeladonCupBlockEntity extends BaseTankContainerBlockEntity {

    public CeladonCupBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.CELADON_CUP_ENTITY.get(), pos, state, 250, 1, 1);
    }

}
