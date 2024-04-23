package cn.solarmoon.idyllic_food_diary.common.block_entity;

import cn.solarmoon.idyllic_food_diary.common.block_entity.base.AbstractServicePlateBlockEntity;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ServicePlateBlockEntity extends AbstractServicePlateBlockEntity {
    public ServicePlateBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.PLATE.get(), pos, state);
    }
}
