package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

public abstract class AbstractConsumeFoodDoubleBlock extends AbstractConsumeFoodBlock implements IBedPartBlock {

    public AbstractConsumeFoodDoubleBlock(int count, int eatCount, int nutrition, float saturation) {
        super(count, eatCount, nutrition, saturation,
                BlockBehaviour.Properties.of()
                        .strength(1)
                        .sound(SoundType.GLASS)
                        .noCollission()
                        .pushReaction(PushReaction.DESTROY));
    }

    public AbstractConsumeFoodDoubleBlock(int count, int eatCount, int nutrition, float saturation, SoundType soundType) {
        super(count, eatCount, nutrition, saturation,
                BlockBehaviour.Properties.of()
                        .strength(1)
                        .sound(soundType)
                        .noCollission()
                        .pushReaction(PushReaction.DESTROY));
    }

    /**
     * @return 同步remain
     */
    @Override
    public BlockState updateShape(BlockState stateIn, Direction direction, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState state = super.updateShape(stateIn, direction, facingState, level, currentPos, facingPos);
        if (state.is(this) && facingState.getValues().get(REMAIN) != null) {
            state = state.setValue(REMAIN, facingState.getValue(REMAIN));
        }
        return state;
    }

}
