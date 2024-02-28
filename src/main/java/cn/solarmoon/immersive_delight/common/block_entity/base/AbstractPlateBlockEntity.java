package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.solarmoon_core.common.block_entity.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractPlateBlockEntity extends BaseContainerBlockEntity {

    public AbstractPlateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, 64, 1, pos, state);
    }

}
