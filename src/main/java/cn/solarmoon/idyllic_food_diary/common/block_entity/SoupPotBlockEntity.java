package cn.solarmoon.idyllic_food_diary.common.block_entity;

import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractSoupPotBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SoupPotBlockEntity extends AbstractSoupPotBlockEntity {

    public SoupPotBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.SOUP_POT.get(), 1000, 9, 1, pos, state);
    }

}
