package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CupBlockEntity extends TankBlockEntity {

    public CupBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.CELADON_CUP_ENTITY.get(), pos, state);
    }

    @Override
    public int getTankMaxCapacity() {
        return Config.maxCeladonCupCapacity.get();
    }

}
