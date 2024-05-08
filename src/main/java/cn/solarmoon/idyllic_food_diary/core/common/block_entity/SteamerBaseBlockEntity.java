package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMFluids;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMFluidTags;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SteamerBaseBlockEntity extends BaseTankBlockEntity implements IKettleRecipe, IBlockEntityAnimateTicker {

    private int boilTime;
    private int recipeTime;
    private int drainTick;
    private int animTick;
    private float last;

    public SteamerBaseBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.STEAMER_BASE.get(), 1000, pos, state);
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
    public int getBoilRecipeTime() {
        return recipeTime;
    }

    @Override
    public int getBoilTime() {
        return boilTime;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        recipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        boilTime = time;
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

}
