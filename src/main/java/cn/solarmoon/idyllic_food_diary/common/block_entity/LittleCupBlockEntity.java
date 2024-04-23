package cn.solarmoon.idyllic_food_diary.common.block_entity;

import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractCupBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LittleCupBlockEntity extends AbstractCupBlockEntity {

    public LittleCupBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.LITTLE_CUP.get(), 250, 1, 1, pos, state);
    }

}
