package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CuttingBoardBlockEntity extends BaseContainerBlockEntity {

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.CUTTING_BOARD.get(), 1, 1, pos, state);
    }

}
