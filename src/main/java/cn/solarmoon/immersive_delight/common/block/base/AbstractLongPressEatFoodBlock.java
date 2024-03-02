package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.item.food_block_item.BowlFoodItem;
import cn.solarmoon.immersive_delight.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.util.BlockUtil;
import cn.solarmoon.solarmoon_core.util.CountingDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.Eat.eat;
import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;


/**
 * 所有长按右键可以吃掉的方块的抽象基本类，用于一些没有进食模型改变的食物方块
 * 一般这种类型的方块所直接对应的物品就是可食用的，因此为了不妨碍进食需要shift放置
 */
public abstract class AbstractLongPressEatFoodBlock extends BaseWaterBlock {

    private final int eatCount;

    /**
     * 默认吃5下，吃完后变为空气方块
     */
    public AbstractLongPressEatFoodBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.eatCount = 4;
    }

    /**
     * @param eatCount 需要右键的次数才能吃，注意这里吃5下需要填4，因为0也算一下
     */
    public AbstractLongPressEatFoodBlock(int eatCount, BlockBehaviour.Properties properties) {
        super(properties);
        this.eatCount = eatCount;
    }

    /**
     * 长按右键吃掉该方块
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (getBlockLeft().asItem().getDefaultInstance().is(IMItemTags.FOOD_CONTAINER) && getThis(player, level, pos, state, hand)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1);
            return InteractionResult.SUCCESS;
        }

        if (player.canEat(false) && !heldItem.is(ROLLING_PIN.get())) {

            //计数装置
            CountingDevice counting = new CountingDevice(player, pos);
            ImmersiveDelight.DEBUG.send("点击次数：" + counting.getCount(), level);

            //吃的声音
            level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            if(level.isClientSide) eatParticle(pos);

            if (counting.getCount() >= this.eatCount) {

                ItemStack food = state.getBlock().asItem().getDefaultInstance();
                //应用汤碗类的fluidEffect
                if (food.getItem() instanceof BowlFoodItem soup) {
                    soup.applyFluidEffect(level, player);
                }
                //吃掉！
                player.eat(level, food);

                //设置结束方块
                BlockState stateTo = getBlockLeft().defaultBlockState();
                if (getBlockLeft() instanceof IBedPartBlock) {
                    BlockUtil.setBedPartBlock(state, stateTo, level, pos);
                } // 双方块特殊放置
                else BlockUtil.setBlockWithDirection(state, stateTo, level, pos);
                //重置计数
                counting.resetCount();
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /**
     * 决定吃的声音类型
     */
    public SoundEvent getEatSound() {
        return SoundEvents.GENERIC_EAT;
    }

    /**
     * @return 决定吃完后留下的方块
     */
    public Block getBlockLeft() {
        return Blocks.AIR;
    }

    /**
     * 决定吃的粒子效果
     */
    public void eatParticle(BlockPos pos) {
        eat(pos);
    }

    /**
     * 放置限制在 完整的方块上方
     */
    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return worldIn.getBlockState(belowPos).isFaceSturdy(worldIn, belowPos, Direction.UP);
    }

    /**
     * shift才能放置
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if(!Objects.requireNonNull(context.getPlayer()).isCrouching()) return null;
        return super.getStateForPlacement(context);
    }

}
