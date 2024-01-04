package cn.solarmoon.immersive_delight.common.blocks.entities;

import cn.solarmoon.immersive_delight.common.blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.common.RegisterBlocks;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CupBlockEntity extends TankBlockEntity {

    public CupBlockEntity(BlockPos pos, BlockState state) {
        super(RegisterBlocks.CUP_ENTITY.get(), pos, state);
    }

    @Override
    public int getTankMaxVolume() {
        return Config.maxCupVolume.get();
    }

}
