package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardBlockEntity extends BaseContainerBlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.CUTTING_BOARD.get(), 9, 1, pos, state);
    }

}
