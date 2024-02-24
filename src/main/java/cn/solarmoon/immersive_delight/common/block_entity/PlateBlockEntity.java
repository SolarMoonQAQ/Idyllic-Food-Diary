package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractPlateBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlateBlockEntity extends AbstractPlateBlockEntity {
    public PlateBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.PLATE.get(), pos, state);
    }
}
