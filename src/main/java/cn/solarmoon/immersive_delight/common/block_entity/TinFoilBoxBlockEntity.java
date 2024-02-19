package cn.solarmoon.immersive_delight.common.block_entity;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractTinFoilBoxBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TinFoilBoxBlockEntity extends AbstractTinFoilBoxBlockEntity {

    public TinFoilBoxBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.TIN_FOIL_BOX.get(), 6, pos, state);
    }

}
