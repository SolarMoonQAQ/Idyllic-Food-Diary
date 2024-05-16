package cn.solarmoon.idyllic_food_diary.api.common.block.food_block;

import cn.solarmoon.idyllic_food_diary.api.common.item.containable.SoupBowlFoodItem;
import cn.solarmoon.idyllic_food_diary.api.util.ParticleSpawner;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMItemTags;
import cn.solarmoon.solarmoon_core.api.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.player.CountingDevice;
import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems.ROLLING_PIN;


/**
 * 所有长按右键可以吃掉的方块的抽象基本类，用于一些没有进食模型改变的食物方块
 * 一般这种类型的方块所直接对应的物品就是可食用的，因此为了不妨碍进食需要shift放置
 */
public abstract class AbstractLongPressEatFoodBlock extends BaseWaterBlock {

    public AbstractLongPressEatFoodBlock(Properties properties) {
        super(properties);
    }

    /**
     * @return 右键多少下吃一次
     */
    public int getEatCount() {
        return 5;
    }

    /**
     * 长按右键吃掉该方块
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.is(ROLLING_PIN.get())) {
            return InteractionResult.PASS;
        }

        if (getBlockLeft().asItem().getDefaultInstance().is(IMItemTags.FOOD_CONTAINER) && getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        if (player.canEat(false)) {

            //计数装置
            CountingDevice counting = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getCountingDevice();
            counting.setCount(counting.getCount() + 1, pos);
            IdyllicFoodDiary.DEBUG.send("点击次数：" + counting.getCount(), player);
            //吃的声音
            level.playSound(null, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            ParticleSpawner.eat(pos, player, level);

            if (counting.getCount() >= getEatCount()) {

                ItemStack food = state.getBlock().asItem().getDefaultInstance();
                //应用汤碗类的fluidEffect
                if (food.getItem() instanceof SoupBowlFoodItem soup) {
                    soup.applyFluidEffect(level, player);
                }
                //吃掉！
                player.eat(level, food);

                //设置结束方块
                BlockState stateTo = getBlockLeft().defaultBlockState();
                BlockUtil.replaceBlockWithAllState(state, stateTo, level, pos);

                //设置结束物品
                LevelSummonUtil.summonDrop(getItemLeft(), level, pos);

                //重置计数
                counting.resetCount();
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME; // 防止右键此类方块时使用手中物品
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
    public abstract Block getBlockLeft();

    public ItemStack getItemLeft() {return ItemStack.EMPTY;}

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
