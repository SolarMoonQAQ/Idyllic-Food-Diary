package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.CupRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public abstract class AbstractCupBlockEntity extends BaseTCBlockEntity implements ITimeRecipeBlockEntity<CupRecipe>, IBlockEntityAnimateTicker {

    private int time;
    private int recipeTime;
    private int ticks;
    private float last;

    public AbstractCupBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, maxCapacity, size, slotLimit, pos, state);
    }

    /**
     * 获取匹配的配方（杯中有物品阶段的匹配），不匹配返回null
     */
    @Override
    public CupRecipe getCheckedRecipe(Level level, BlockPos pos) {
        FluidStack fluidStack = getTank().getFluid();
        ItemStack stackIn = getInventory().getStackInSlot(0);
        List<CupRecipe> recipes = RecipeUtil.getRecipes(level, IMRecipes.CUP.get());
        for (var recipe : recipes) {
            if (recipe.inputMatches(level, fluidStack, stackIn, pos)) {
                return recipe;
            }
        }
        return null;
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

    @Override
    public int getTicks() {
        return ticks;
    }

    @Override
    public void setTicks(int i) {
        ticks = i;
    }

    @Override
    public float getLast() {
        return last;
    }

    @Override
    public void setLast(float last) {
        this.last = last;
    }

}
