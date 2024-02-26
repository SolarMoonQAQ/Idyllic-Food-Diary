package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSoupPotBlockEntity;
import cn.solarmoon.immersive_delight.common.recipe.SoupPotRecipe;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseTCEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;

/**
 * 汤锅
 */
public abstract class AbstractSoupPotEntityBlock extends BaseTCEntityBlock {


    protected AbstractSoupPotEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if(blockEntity == null) return InteractionResult.FAIL;

        //空手shift+右键快速拿
        if(getThis(player, level, pos, state, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        //能够存取液体
        if (putFluid(blockEntity, player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        if (takeFluid(blockEntity, player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }

        //存取任意单个物品
        if(storage(blockEntity, player, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if (blockEntity instanceof AbstractSoupPotBlockEntity soupPot) {
            SoupPotRecipe recipe = soupPot.getCheckedRecipe();
            int time = soupPot.getTime();
            if (recipe != null) {
                time++;
                soupPot.setRecipeTime(recipe.getTime());
                if(time >= recipe.getTime()) {
                    FluidStack newF = new FluidStack(recipe.getOutputFluid(), recipe.outputFluidAmount);
                    soupPot.setFluid(newF);
                    //清除所有满足配方输入的输入物
                    for (var in :recipe.getInputIngredients()) {
                        for (var stack : soupPot.getStacks()) {
                            if (in.test(stack)) {
                                stack.shrink(1);
                            }
                        }
                    }
                    //输出物
                    if (!recipe.outputItems.isEmpty()) {
                        for (var out : recipe.outputItems) {
                            soupPot.insertItem(out.getDefaultInstance());
                        }
                    }
                    soupPot.setTime(0);
                    soupPot.setChanged();
                }
                soupPot.setTime(time);
            } else {
                soupPot.setTime(0);
                soupPot.setRecipeTime(0);
            }
        }
    }

}
