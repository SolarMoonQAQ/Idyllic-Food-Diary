package cn.solarmoon.idyllic_food_diary.common.block_entity.base;

import cn.solarmoon.idyllic_food_diary.common.block_entity.iutor.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.common.recipe.SoupPotRecipe;
import cn.solarmoon.idyllic_food_diary.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSoupPotBlockEntity extends BaseTCBlockEntity implements ITimeRecipeBlockEntity<SoupPotRecipe>, IKettleRecipe, IBlockEntityAnimateTicker {

    private int time;
    private int recipeTime;

    private int animTick;
    private float last;

    public int boilTime;
    public int boilRecipeTime;

    public AbstractSoupPotBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, maxCapacity, size, slotLimit, pos, state);
    }

    public void tryCook() {
        Optional<SoupPotRecipe> recipeOp = getCheckedRecipe();
        int time = getTime();
        if (recipeOp.isPresent()) {
            SoupPotRecipe recipe = recipeOp.get();
            time++;
            setRecipeTime(recipe.time());
            if(time >= recipe.time()) {
                setFluid(recipe.outputFluid().copy());
                //清除所有满足配方输入的输入物
                for (var in :recipe.ingredients()) {
                    for (var stack : getStacks()) {
                        if (in.test(stack)) {
                            stack.shrink(1);
                        }
                    }
                }
                //输出物
                if (!recipe.outputItems().isEmpty()) {
                    for (var out : recipe.outputItems()) {
                        insertItem(out.copy());
                    }
                }
                setTime(0);
                setChanged();
            }
            setTime(time);
        } else {
            setTime(0);
            setRecipeTime(0);
            tryBoilWater();
        }
    }

    public boolean isCooking() {
        return time > 0;
    }

    /**
     * @return 液体是否正被加热（是否有液体且下方是否为热源）
     */
    public boolean isHeatingFluid() {
        return level != null && !getTank().isEmpty() && FarmerUtil.isHeatSource(level.getBlockState(getBlockPos().below()));
    }

    /**
     * 获取首个匹配的配方
     */
    @Override
    public Optional<SoupPotRecipe> getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return Optional.empty();
        List<SoupPotRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_POT.get());
        return recipes.stream().filter(recipe -> {
            /*
             * 要求：
             * 输入物完全匹配
             * 输入液体及量完全匹配
             * 下方为热源
             */
            List<ItemStack> stacks = getStacks();
            if (RecipeMatcher.findMatches(stacks, recipe.ingredients()) != null) {
                FluidStack ctStack = getTank().getFluid();
                if (FluidUtil.isMatch(ctStack, recipe.inputFluid(), true, recipe.compareNBT())) {
                    if (getLevel() != null) {
                        return FarmerUtil.isHeatSource(getLevel().getBlockState(getBlockPos().below()));
                    }
                }
            }
            return false;
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
        return animTick;
    }

    @Override
    public void setTicks(int ticks) {
        animTick = ticks;
    }

    @Override
    public float getLast() {
        return last;
    }

    @Override
    public void setLast(float last) {
        this.last = last;
    }

    @Override
    public int getBoilRecipeTime() {
        return boilRecipeTime;
    }

    @Override
    public int getBoilTime() {
        return boilTime;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        boilRecipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        boilTime = time;
    }

}
