package cn.solarmoon.immersive_delight.common.blocks.abstract_blocks;

import cn.solarmoon.immersive_delight.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.solarmoon.immersive_delight.common.RegisterItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.particles.methods.Eat.eat;


/**
 * 所有长按右键可以吃掉的方块的抽象基本类，用于一些没有进食模型改变的食物方块
 */
public abstract class LongPressEatBlock extends BasicBlock {

    public LongPressEatBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * 长按右键吃掉该方块
     */
    private long lastPressTime = 0;
    private int pressCount = 0;
    private BlockPos lastPos;
    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player.canEat(false) && !player.getItemInHand(hand).is(ROLLING_PIN.get())) {
            if(level.isClientSide) {
                if (level.getGameTime() - lastPressTime > 5 || !pos.equals(lastPos)) {
                    pressCount = 0;
                }
                pressCount++;

                //吃的声音
                level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
                //吃的粒子效果
                eatParticle(pos);

                lastPressTime = level.getGameTime();
                lastPos = pos;

                if (pressCount >= getEatCount()) {
                    //吃掉！
                    ServerPackSerializer.sendPacket(pos, null, null, "eat");
                    ServerPackSerializer.sendPacket(pos, getBlockAfterEat(), null, "finished");
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    /**
     * 决定要吃多少下才能吃完
     */
    public int getEatCount(){
        return 5;
    }

    /**
     * 决定吃完后变为的方块
     */
    public Block getBlockAfterEat() {
        return Blocks.AIR;
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
     * 碰撞箱
     */
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
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
