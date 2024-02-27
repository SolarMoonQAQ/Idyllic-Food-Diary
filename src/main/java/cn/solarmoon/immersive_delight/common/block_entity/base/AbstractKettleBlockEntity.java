package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.KettleRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class AbstractKettleBlockEntity extends BaseTankBlockEntity implements ITimeRecipeBlockEntity<KettleRecipe> {

    private int time;
    private int recipeTime;

    private int lastFluidAmount;

    public AbstractKettleBlockEntity(BlockEntityType<?> type, int maxCapacity, BlockPos pos, BlockState state) {
        super(type, maxCapacity, pos, state);
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
     * 遍历所有配方检测液体是否匹配input且下方为热源<br/>
     * 返回匹配的配方
     */
    @Override
    public KettleRecipe getCheckedRecipe() {
        Level level = getLevel();
        if (level == null) return null;
        BlockPos pos = getBlockPos();
        //液体量改变时配方时间重置
        int amount = getTank().getFluidAmount();
        if (amount != lastFluidAmount) {
            lastFluidAmount = amount;
            return null;
        }
        for (KettleRecipe kettleRecipe : RecipeUtil.getRecipes(level, IMRecipes.KETTLE.get())) {
            if(kettleRecipe.inputMatches(level, pos)) {
                return kettleRecipe;
            }
        }
        return null;
    }

    /**
     * 接受的水壶配方<br/>
     * 用于tick中，连接底部为热源，连接底部为水就尝试把水烧开<br/>
     * 同时要防止多个蒸笼同时执行这个操作
     */
    public void tryBoilWater() {
        KettleRecipe kettleRecipe = getCheckedRecipe();
        Level level = getLevel();
        BlockPos pos = getBlockPos();
        if (level == null) return;
        int time = getTime();
        if (kettleRecipe != null) {
            setRecipeTime(kettleRecipe.getActualTime(level, pos));
            time++;
            if (time > kettleRecipe.getActualTime(level, pos)) {
                FluidStack fluidStack = new FluidStack(kettleRecipe.getOutputFluid(), getTank().getFluidAmount());
                getTank().setFluid(fluidStack);
                time = 0;
                setChanged();
            }
            setTime(time);
        } else {
            setTime(0);
            setRecipeTime(0);
        }
    }

}
