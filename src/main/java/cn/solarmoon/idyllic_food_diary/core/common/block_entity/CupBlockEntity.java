package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.core.common.recipe.CupRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class CupBlockEntity extends BaseTCBlockEntity implements ITimeRecipeBlockEntity<CupRecipe>, IBlockEntityAnimateTicker {

    private int time;
    private int recipeTime;
    private int ticks;
    private float last;

    public CupBlockEntity(int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(IMBlockEntities.LITTLE_CUP.get(), maxCapacity, size, slotLimit, pos, state);
    }

    public void tryMakeTea() {
        int time = getTime();
        Optional<CupRecipe> recipeOp = getCheckedRecipe();
        if(recipeOp.isPresent()) {
            CupRecipe recipe = recipeOp.get();
            setRecipeTime(recipe.time());
            time++;
            if (time > recipe.time()) {
                getTank().setFluid(recipe.outputFluid().copy());
                setTime(0);
                extractItem(1);
                setChanged();
            }
            setTime(time);
        } else {
            setTime(0);
            setRecipeTime(0);
        }
    }

    /**
     * 获取匹配的配方（杯中有物品阶段的匹配），不匹配返回null
     */
    @Override
    public Optional<CupRecipe> getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return Optional.empty();
        FluidStack fluidStack = getTank().getFluid();
        ItemStack stackIn = getInventory().getStackInSlot(0);
        List<CupRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.CUP.get());
        return recipes.stream().filter(recipe -> {
            boolean match = FluidUtil.isMatch(fluidStack, recipe.inputFluid(), true, recipe.compareNBT());
            return match && recipe.ingredient().test(stackIn);
        }).findFirst();
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
