package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.SoupPotRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;

public abstract class AbstractSoupPotBlockEntity extends BaseTCBlockEntity implements ITimeRecipeBlockEntity<SoupPotRecipe>, IContainerBlockEntity {

    private int time;
    private int recipeTime;
    private int lastFluidAmount;

    public AbstractSoupPotBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, maxCapacity, size, slotLimit, pos, state);
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void setRecipeTime(int recipeTime) {
        this.recipeTime = recipeTime;
    }

    /**
     * 获取首个匹配的配方
     */
    @Override
    public SoupPotRecipe getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return null;
        BlockPos pos = getBlockPos();
        List<SoupPotRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_POT.get());
        //液体量改变时配方时间重置
        int amount = getTank().getFluidAmount();
        if (amount != lastFluidAmount) {
            lastFluidAmount = amount;
            return null;
        }
        for (var recipe : recipes) {
            if (recipe.inputMatches(this, level, pos)) {
                return recipe;
            }
        }
        return null;
    }

}
