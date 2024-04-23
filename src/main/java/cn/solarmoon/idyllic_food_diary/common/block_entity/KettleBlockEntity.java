package cn.solarmoon.idyllic_food_diary.common.block_entity;

import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractKettleBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class KettleBlockEntity extends AbstractKettleBlockEntity {

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.KETTLE.get(), 1000, pos, state);
    }

}
