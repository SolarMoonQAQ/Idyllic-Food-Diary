package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block;

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseInteractionBlock extends FoodEntityBlock {

    public static final IntegerProperty INTERACTION = IntegerProperty.create("interaction", 0, 10);

    public BaseInteractionBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(INTERACTION, getMaxInteraction()));
    }

    /**
     * @return 决定可交互的次数
     */
    public abstract int getMaxInteraction();

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        putRemainToStack(state, stack);
        return stack;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            CompoundTag tag = context.getItemInHand().getTag();
            if (tag != null && tag.contains("Interaction")) { // 不用getOrCreate保证不对tag作任何修改
                int remain = tag.getInt("Interaction");
                state = state.setValue(INTERACTION, remain);
            } else state = state.setValue(INTERACTION, getMaxInteraction());
        }
        return state;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> stacks = super.getDrops(state, builder);
        for (var stack :stacks) {
            if (stack.is(asItem())) {
                putRemainToStack(state, stack);
            }
        }
        return stacks;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(INTERACTION);
    }

    /**
     * @return 如果是双方块则同步对面的remain
     */
    @Override
    public BlockState updateShape(BlockState stateIn, Direction direction, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState state = super.updateShape(stateIn, direction, facingState, level, currentPos, facingPos);
        if (this instanceof IBedPartBlock partBlock) {
            if (direction == partBlock.getNeighbourDirection(stateIn.getValue(IBedPartBlock.PART), stateIn.getValue(IHorizontalFacingBlock.FACING))) {
                if (state.is(this) && facingState.getValues().get(INTERACTION) != null) {
                    state = state.setValue(INTERACTION, facingState.getValue(INTERACTION));
                }
            }
        }
        return state;
    }

    /**
     * 将remain数存入stack<br/>
     * 需要注意的是，这里为了更为通用且防止挖掉的方块在最大remain数情况下无法和相同remain数的物品堆叠，因此默认如果是最大remain数，就不存入任何信息，加载的时候也按此逻辑读取最大remain数。
     * @param state 从state中读取remain数
     * @param stack 要存入remain tag的物品
     */
    public void putRemainToStack(BlockState state, ItemStack stack) {
        if (state.getValue(INTERACTION) < getMaxInteraction()) {
            stack.getOrCreateTag().putInt("Interaction", state.getValue(INTERACTION));
        }
    }

    /**
     * 红石信号规则：根据剩余阶段比例数减小
     * 也就是放出来满格，越拿越少
     */
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (int) ((state.getValue(INTERACTION) / (float) getMaxInteraction()) * 15);
    }

}
