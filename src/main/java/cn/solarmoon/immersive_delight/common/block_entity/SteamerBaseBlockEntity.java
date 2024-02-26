package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSteamerBaseBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SteamerBaseBlockEntity extends AbstractSteamerBaseBlockEntity {

    public SteamerBaseBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.STEAMER_BASE.get(), pos, state);
    }

}
