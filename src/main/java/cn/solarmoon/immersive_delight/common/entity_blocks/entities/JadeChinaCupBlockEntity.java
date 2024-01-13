package cn.solarmoon.immersive_delight.common.entity_blocks.entities;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.CupBlockEntity;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class JadeChinaCupBlockEntity extends CupBlockEntity {

    public JadeChinaCupBlockEntity(BlockPos pos, BlockState state) {
        super(IMEntityBlocks.JADE_CHINA_CUP_ENTITY.get() , pos, state);
    }

    @Override
    public int getTankMaxCapacity() {
        return Config.maxJadeChinaCupCapacity.get();
    }

}
