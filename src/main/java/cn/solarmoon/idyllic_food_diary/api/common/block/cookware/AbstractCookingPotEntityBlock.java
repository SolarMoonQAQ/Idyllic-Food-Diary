package cn.solarmoon.idyllic_food_diary.api.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.core.common.block_entity.CookingPotBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 汤锅
 */
public abstract class AbstractCookingPotEntityBlock extends BasicEntityBlock {


    protected AbstractCookingPotEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CookingPotBlockEntity cookingPot = (CookingPotBlockEntity) level.getBlockEntity(pos);
        if(cookingPot == null) return InteractionResult.PASS;

        if (cookingPot.tryGiveResult(player, hand)) return InteractionResult.SUCCESS;

        //空手shift+右键快速拿
        if(getThis(player, level, pos, state, hand, false)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!cookingPot.hasResult()) {
            //能够存取液体
            if (cookingPot.putFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }
            if (cookingPot.takeFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand.equals(InteractionHand.MAIN_HAND) && cookingPot.storage(player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        CookingPotBlockEntity soupPot = (CookingPotBlockEntity) blockEntity;
        if (!soupPot.tryCook()) {
            soupPot.tryBoilWater();
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity soupPot) {
            if (soupPot.isHeatingFluid()) {
                if (random.nextInt(10) < 4) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), 0, 0.1, 0);
                }
            }
        }
    }
}
