package cn.solarmoon.idyllic_food_diary.api.common.block.entity_block;

import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
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

public abstract class AbstractCuttingBoardEntityBlock extends BasicEntityBlock {

    public AbstractCuttingBoardEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IContainerBlockEntity cb) {
            if (cb.putItem(player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.PLAYERS);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
            if (cb.takeItem(player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.PLAYERS);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

}
