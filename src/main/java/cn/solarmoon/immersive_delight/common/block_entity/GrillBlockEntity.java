package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractGrillBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GrillBlockEntity extends AbstractGrillBlockEntity {

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.GRILL.get(), pos, state);
    }

}
