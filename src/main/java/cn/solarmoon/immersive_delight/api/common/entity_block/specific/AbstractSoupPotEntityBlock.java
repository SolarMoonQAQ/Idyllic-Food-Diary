package cn.solarmoon.immersive_delight.api.common.entity_block.specific;

import cn.solarmoon.immersive_delight.api.common.entity_block.BaseContainerTankEntityBlock;
import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseContainerTankBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.common.recipes.SoupPotRecipe;
import cn.solarmoon.immersive_delight.util.RecipeHelper;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
public abstract class AbstractSoupPotEntityBlock extends BaseContainerTankEntityBlock {


    protected AbstractSoupPotEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BaseTankBlockEntity tankEntity = (BaseTankBlockEntity) blockEntity;
        ItemStack heldItem = player.getItemInHand(hand);
        if(tankEntity == null) return InteractionResult.FAIL;

        //空手shift+右键快速拿
        if(getThis(hand, heldItem, player, level, pos, state)) {
            level.playSound(null, pos, SoundEvents.LANTERN_BREAK, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        //能够存取液体
        if (loadFluid(heldItem, tankEntity, player, level, pos, hand)) return InteractionResult.SUCCESS;

        //存取任意单个物品
        if(storage(blockEntity, heldItem, player, hand)) {
            level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);

        if (blockEntity instanceof BaseContainerTankBlockEntity ct) {
            SoupPotRecipe recipe = getCheckedRecipe(ct, level, pos);
            if (recipe != null) {
                ct.time++;
                Util.deBug("Time：" + ct.time, level);
                if(ct.time >= recipe.getTime()) {
                    FluidStack newF = new FluidStack(recipe.getOutputFluid(), recipe.outputFluidAmount);
                    ct.setFluid(newF);
                    for (var in :recipe.getInputIngredients()) {
                        for (var stack : ct.getStacks()) {
                            if (in.test(stack)) stack.shrink(1);
                        }
                    }
                    ct.time = 0;
                }
            } else ct.time = 0;
        }
    }

    /**
     * 获取首个匹配的配方
     */
    public SoupPotRecipe getCheckedRecipe(BaseContainerTankBlockEntity ct, Level level, BlockPos pos) {
        List<SoupPotRecipe> recipes = RecipeHelper.GetRecipes.SoupPotRecipe(level);
        for (var recipe :recipes) {
            if(recipe.inputMatches(ct, new RecipeWrapper(ct.inventory), level, pos)) {
                return recipe;
            }
        }
        return null;
    }

}
