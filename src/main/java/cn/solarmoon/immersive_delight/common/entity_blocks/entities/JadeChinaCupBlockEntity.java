package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankContainerBlockEntity;
import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class JadeChinaCupBlockEntity extends BaseTankContainerBlockEntity {

    public JadeChinaCupBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.JADE_CHINA_CUP_ENTITY.get() , pos, state, 250, 1, 1);
    }

}
