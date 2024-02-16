package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSoupPotBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.IContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupPotBlockEntity extends AbstractSoupPotBlockEntity implements IContainerBlockEntity {

    public SoupPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.SOUP_POT.get(), 2000, 9, 1, pos, state);
    }

}
