package cn.solarmoon.immersive_delight.common.blocks;

import cn.solarmoon.immersive_delight.common.blocks.abstract_blocks.DrinkableEntityBlock;
import cn.solarmoon.immersive_delight.common.blocks.entities.CupBlockEntity;
import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Objects;

import static cn.solarmoon.immersive_delight.common.RegisterBlocks.CUP_ENTITY;

public class CupEntityBlock extends DrinkableEntityBlock {

    public CupEntityBlock() {
        super(Block.Properties.of().sound(SoundType.GLASS).strength(0f));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CupBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return CUP_ENTITY.get();
    }

}
