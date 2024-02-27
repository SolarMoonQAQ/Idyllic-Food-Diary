package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractCuttingBoardEntityBlock extends BaseContainerEntityBlock {

    public AbstractCuttingBoardEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return InteractionResult.PASS;

        if (putItem(blockEntity, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.PLAYERS);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        if (takeItem(blockEntity, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.PLAYERS);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

}
