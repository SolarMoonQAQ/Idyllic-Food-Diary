package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.recipe.KettleRecipe;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import cn.solarmoon.immersive_delight.common.registry.IMRecipes;
import cn.solarmoon.immersive_delight.data.tags.IMFluidTags;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class AbstractSteamerBaseBlockEntity extends BaseTankBlockEntity implements ITimeRecipeBlockEntity<KettleRecipe> {

    private int boilTime;
    private int recipeTime;
    private int drainTick;

    public AbstractSteamerBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, 2000, pos, state);
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

    /**
     * 用于tick中，连接底部为热源，连接底部为热水就每秒消耗热水<br/>
     * 同时要防止多个蒸笼同时执行这个操作
     */
    public void tryDrainHotFluid() {
        if (isEvaporating()) {
            drainTick++;
            if (drainTick >= 20) {
                drainTick = 0;
                getTank().drain(5, IFluidHandler.FluidAction.EXECUTE);
                setChanged();
            }
        } else {
            drainTick = 0;
        }
    }

    public boolean hasHotWater() {
        return getTank().getFluid().getFluid().getFluidType().equals(IMFluids.HOT_WATER.getType());
    }

    public boolean hasHotFluid() {
        return getTank().getFluid().getFluid().is(IMFluidTags.HOT_FLUID);
    }

    public boolean hasHeatSource() {
        if (getLevel() == null) return false;
        return FarmerUtil.isHeatSource(getLevel().getBlockState(getBlockPos().below()));
    }

    /**
     * @return 是否正在烧水
     */
    public boolean isBoiling() {
        return boilTime != 0;
    }

    /**
     * @return 是否正在蒸发液体
     */
    public boolean isEvaporating() {
        return hasHeatSource() && hasHotFluid();
    }

    /**
     * @return 是否可供给蒸笼工作
     */
    public boolean isWorking() {
        return hasHeatSource() && hasHotWater();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(NBTList.DRAIN_TICK, drainTick);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        drainTick = nbt.getInt(NBTList.DRAIN_TICK);
    }

    @Override
    public int getTime() {
        return boilTime;
    }

    @Override
    public int getRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setTime(int i) {
        boilTime = i;
    }

    @Override
    public void setRecipeTime(int i) {
        recipeTime = i;
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
        for (KettleRecipe kettleRecipe : RecipeUtil.getRecipes(level, IMRecipes.KETTLE.get())) {
            if(kettleRecipe.inputMatches(level, pos)) {
                return kettleRecipe;
            }
        }
        return null;
    }

}
