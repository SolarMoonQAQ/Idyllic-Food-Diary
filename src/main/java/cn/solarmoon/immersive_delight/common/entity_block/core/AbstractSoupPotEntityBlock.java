package cn.solarmoon.immersive_delight.common.entity_block.core;

import cn.solarmoon.immersive_delight.api.common.entity_block.BaseTCEntityBlock;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.common.recipes.SoupPotRecipe;
import cn.solarmoon.immersive_delight.api.util.RecipeUtil;
import cn.solarmoon.immersive_delight.util.CoreUtil;
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
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;

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
        BaseTankBlockEntity tankEntity = (BaseTankBlockEntity) blockEntity;
        if(tankEntity == null) return InteractionResult.FAIL;

        //空手shift+右键快速拿
        if(getThis(player, level, pos, state, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        //能够存取液体
        if (loadFluid(tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;

        //存取任意单个物品
        if(storage(blockEntity, player, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if (blockEntity instanceof BaseTCBlockEntity tc) {
            SoupPotRecipe recipe = getCheckedRecipe(tc, level, pos);
            if (recipe != null) {
                tc.time++;
                CoreUtil.deBug("Time：" + tc.time, level);
                if(tc.time >= recipe.getTime()) {
                    FluidStack newF = new FluidStack(recipe.getOutputFluid(), recipe.outputFluidAmount);
                    tc.setFluid(newF);
                    //清除所有满足配方输入的输入物
                    for (var in :recipe.getInputIngredients()) {
                        for (var stack : tc.getStacks()) {
                            if (in.test(stack)) {
                                stack.shrink(1);
                            }
                        }
                    }
                    //输出物
                    if (!recipe.outputItems.isEmpty()) {
                        for (var out : recipe.outputItems) {
                            tc.insertItem(out.getDefaultInstance());
                        }
                    }
                    tc.time = 0;
                    tc.setChanged();
                }
            } else tc.time = 0;
        }
    }

    /**
     * 获取首个匹配的配方
     */
    public SoupPotRecipe getCheckedRecipe(BaseTCBlockEntity ct, Level level, BlockPos pos) {
        List<SoupPotRecipe> recipes = RecipeUtil.getRecipes(level, IMRecipes.SOUP_POT_RECIPE.get());
        for (var recipe :recipes) {
            if(recipe.inputMatches(ct, new RecipeWrapper(ct.inventory), level, pos)) {
                return recipe;
            }
        }
        return null;
    }

}
