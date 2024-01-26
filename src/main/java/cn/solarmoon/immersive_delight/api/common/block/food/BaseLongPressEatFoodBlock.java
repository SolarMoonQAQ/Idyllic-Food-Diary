package cn.solarmoon.immersive_delight.api.common.block.food;

import cn.solarmoon.immersive_delight.api.common.block.BaseWaterBlock;
import cn.solarmoon.immersive_delight.api.common.item.specific.AbstractBowlFoodItem;
import cn.solarmoon.immersive_delight.api.common.item.specific.AbstractCupItem;
import cn.solarmoon.immersive_delight.util.CountingDevice;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.client.particles.vanilla.Eat.eat;


/**
 * 所有长按右键可以吃掉的方块的抽象基本类，用于一些没有进食模型改变的食物方块
 * 一般这种类型的方块所直接对应的物品就是可食用的，因此为了不妨碍进食需要shift放置
 */
public abstract class BaseLongPressEatFoodBlock extends BaseWaterBlock {

    private final int eatCount;
    private final Block blockLeft;

    /**
     * 默认吃5下，吃完后变为空气方块
     */
    public BaseLongPressEatFoodBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.eatCount = 4;
        this.blockLeft = Blocks.AIR;
    }

    /**
     * @param blockLeft 吃完后需要替换的方块
     */
    public BaseLongPressEatFoodBlock(Block blockLeft, BlockBehaviour.Properties properties) {
        super(properties);
        this.eatCount = 4;
        this.blockLeft = blockLeft;
    }

    /**
     * @param eatCount 需要右键的次数才能吃，注意这里吃5下需要填4，因为0也算一下
     * @param blockLeft 吃完后需要替换的方块
     */
    public BaseLongPressEatFoodBlock(int eatCount, Block blockLeft, BlockBehaviour.Properties properties) {
        super(properties);
        this.eatCount = eatCount;
        this.blockLeft = blockLeft;
    }

    /**
     * 长按右键吃掉该方块
     */
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (player.canEat(false) && !heldItem.is(ROLLING_PIN.get())) {

            //计数装置
            CompoundTag playerTag = CountingDevice.player(player, pos, level);
            Util.deBug("点击次数：" + CountingDevice.getCount(playerTag), level);

            //吃的声音
            level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            if(level.isClientSide) eatParticle(pos);

            if (CountingDevice.getCount(playerTag) >= this.eatCount) {

                ItemStack food = state.getBlock().asItem().getDefaultInstance();
                //同时应用finishUsing方法，这样可以应用fluidEffect
                food.getItem().finishUsingItem(food, level, player);
                //吃掉！
                player.eat(level, food);

                //设置结束方块
                level.setBlock(pos, this.blockLeft.defaultBlockState(), 3);
                //重置计数
                CountingDevice.resetCount(playerTag);
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
