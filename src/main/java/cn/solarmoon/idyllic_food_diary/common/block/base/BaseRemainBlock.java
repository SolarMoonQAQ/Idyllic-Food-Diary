package cn.solarmoon.idyllic_food_diary.common.block.base;

import cn.solarmoon.solarmoon_core.api.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseRemainBlock extends BaseWaterBlock {

    public static final IntegerProperty REMAIN = IntegerProperty.create("remain", 0, 10);

    public BaseRemainBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(REMAIN, getMaxRemain()));
    }

    /**
     * @return 决定可以吃的次数
     */
    public abstract int getMaxRemain();

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        stack.getOrCreateTag().putInt("Remain", state.getValue(REMAIN));
        return stack;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        int remain = context.getItemInHand().getOrCreateTag().getInt("Remain");
        if (remain == 0) remain = getMaxRemain();
        if (state != null) {
            state = state.setValue(REMAIN, remain);
        }
        return state;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> stacks = super.getDrops(state, builder);
        for (var stack :stacks) {
            if (stack.is(asItem())) stack.getOrCreateTag().putInt("Remain", state.getValue(REMAIN));
        }
        return stacks;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(REMAIN);
    }

    /**
     * @return 如果是双方块则同步对面的remain
     */
    @Override
    public BlockState updateShape(BlockState stateIn, Direction direction, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState state = super.updateShape(stateIn, direction, facingState, level, currentPos, facingPos);
        if (this instanceof IBedPartBlock partBlock) {
            if (direction == partBlock.getNeighbourDirection(stateIn.getValue(IBedPartBlock.PART), stateIn.getValue(IHorizontalFacingBlock.FACING))) {
                if (state.is(this) && facingState.getValues().get(REMAIN) != null) {
                    state = state.setValue(REMAIN, facingState.getValue(REMAIN));
                }
            }
        }
        return state;
    }

}
