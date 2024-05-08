package cn.solarmoon.idyllic_food_diary.api.common.block.container;

import cn.solarmoon.solarmoon_core.api.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractLongContainerBlock extends BaseWaterBlock implements IBedPartBlock {

    public AbstractLongContainerBlock(SoundType soundType) {
        super(BlockBehaviour.Properties.of()
                .strength(1)
                .sound(soundType)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
        );
    }

    /**
     * 可以快速拿起
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

}
