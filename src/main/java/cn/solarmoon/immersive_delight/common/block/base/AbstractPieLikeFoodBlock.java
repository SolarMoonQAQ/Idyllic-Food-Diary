package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public abstract class AbstractPieLikeFoodBlock extends BaseWaterBlock {

    public static final IntegerProperty REMAIN = IntegerProperty.create("remain", 0, 10);

    private final Block blockLeft;
    private final int count;

    /**
     * @param count 能吃的次数（分割形态数）
     */
    public AbstractPieLikeFoodBlock(int count, Properties properties) {
        super(properties);
        this.count = count;
        this.blockLeft = Blocks.AIR;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(REMAIN, count)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    /**
     * @param blockLeft 吃完后剩下的方块
     * @param count 能吃的次数（分割形态数）
     */
    public AbstractPieLikeFoodBlock(Block blockLeft, int count, Properties properties) {
        super(properties);
        this.count = count;
        this.blockLeft = blockLeft;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(REMAIN, count)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.canEat(false)) {

            int remain = state.getValue(REMAIN);
            if (remain > 0) {
                BlockState targetState = state.setValue(REMAIN, remain - 1);
                level.setBlock(pos, targetState, 3);
                player.eat(level, this.asItem().getDefaultInstance());

                if (targetState.getValue(REMAIN) == 0) {
                    level.setBlock(pos, blockLeft.defaultBlockState(), 3);
                }

                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    public List<Property<?>> getProperties() {
        List<Property<?>> properties = super.getProperties();
        properties.add(REMAIN);
        return properties;
    }


}
