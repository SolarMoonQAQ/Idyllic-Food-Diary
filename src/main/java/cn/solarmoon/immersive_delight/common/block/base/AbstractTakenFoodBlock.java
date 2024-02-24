package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * 基本的拿取类型的食物
 * 可选拿取所需的物品
 * 根据拿取数改变形态
 */
public abstract class AbstractTakenFoodBlock extends BaseWaterBlock {

    public static final IntegerProperty REMAIN = IntegerProperty.create("remain", 0, 10);

    public final int maxRemain;
    //必须用item而不是itemStack
    public final Item food;
    public final Item container;
    public final Block leftBlock;

    /**
     * @param maxRemain 食物可被获取次数（形态数）
     * @param food 右键所获得的食物
     * @param container 获得食物所需的容器（需要拿着右键才能获取食物的物品）
     *                  为空的话则任何手都能拿
     *                  默认情况下吃完后所设置的方块为container的block形式
     */
    public AbstractTakenFoodBlock(int maxRemain, Item food, Item container, Properties properties) {
        super(properties);
        this.maxRemain = maxRemain;
        this.food = food;
        this.container = container;
        this.leftBlock = Block.byItem(container);
        this.registerDefaultState(this.getStateDefinition().any().setValue(REMAIN, maxRemain));
    }

    /**
     * @param maxRemain 食物可被获取次数（形态数）
     * @param food 右键所获得的食物
     * @param container 获得食物所需的容器（需要拿着右键才能获取食物的物品）
     *                  为空的话则任何手都能拿
     *                  默认情况下吃完后所设置的方块为container的block形式
     * @param leftBlock 吃完后所剩下的方块
     */
    public AbstractTakenFoodBlock(int maxRemain, Item food, Item container, Block leftBlock, Properties properties) {
        super(properties);
        this.maxRemain = maxRemain;
        this.food = food;
        this.container = container;
        this.leftBlock = leftBlock;
        this.registerDefaultState(this.getStateDefinition().any().setValue(REMAIN, maxRemain));
    }

    /**
     * @param maxRemain 食物可被获取次数（形态数）
     * @param food 右键所获得的食物
     */
    public AbstractTakenFoodBlock(int maxRemain, Item food, Properties properties) {
        super(properties);
        this.maxRemain = maxRemain;
        this.food = food;
        this.container = Items.AIR;
        this.leftBlock = Block.byItem(container);
        this.registerDefaultState(this.getStateDefinition().any().setValue(REMAIN, maxRemain));
    }

    public ItemStack getFood() {return new ItemStack(food);}

    public ItemStack getContainer() {return new ItemStack(container);}

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        int remain = state.getValue(REMAIN);
        if (remain > 0) {
            if ((heldItem.is(this.container) || this.container.equals(Items.AIR))) {

                BlockState targetState = state.setValue(REMAIN, remain - 1);

                level.setBlock(pos, targetState, 3);

                if (targetState.getValue(REMAIN) == 0) {
                    level.setBlock(pos, leftBlock.defaultBlockState(), 3);
                }

                if (!this.container.equals(Items.AIR)) heldItem.shrink(1);

                LevelSummonUtil.addItemToInventory(player, this.getFood());

                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS);

                return InteractionResult.SUCCESS;
            } else player.displayClientMessage(ImmersiveDelight.TRANSLATOR.set("message", "container_required", this.getContainer().getHoverName()), true);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(REMAIN);
    }

    /**
     * 只能生存在有足够碰撞箱的方块上
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    /**
     * 红石信号规则：根据剩余可拿取食物比例数减小
     * 也就是放出来满格，越拿越少
     */
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (int) ((state.getValue(REMAIN) / (float) this.maxRemain) * 15);
    }

    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

}
