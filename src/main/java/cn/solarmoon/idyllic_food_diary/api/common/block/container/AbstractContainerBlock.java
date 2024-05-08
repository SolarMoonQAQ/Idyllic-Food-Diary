package cn.solarmoon.idyllic_food_diary.api.common.block.container;

import cn.solarmoon.solarmoon_core.api.common.block.BaseWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractContainerBlock extends BaseWaterBlock {

    public AbstractContainerBlock(SoundType soundType) {
        super(BlockBehaviour.Properties.of()
                .sound(soundType)
                .strength(0.7F)
                .noOcclusion()
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (getThis(player, level, pos, state, hand, true)) return InteractionResult.SUCCESS;
        return super.use(state, level, pos, player, hand, hit);
    }

}
