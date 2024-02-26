package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import cn.solarmoon.immersive_delight.util.FarmerUtil;
import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.common.block_entity.BaseTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class AbstractSteamerBaseBlockEntity extends BaseTankBlockEntity {

    public int boilTick;
    public int drainTick;
    private int lastFluidAmount;

    public AbstractSteamerBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, 2000, pos, state);
    }

    /**
     * 用于tick中，连接底部为热源，连接底部为水就尝试把水烧开<br/>
     * 同时要防止多个蒸笼同时执行这个操作
     */
    public void tryBoilWater() {
        //液体量改变时配方时间重置
        int amount = getTank().getFluidAmount();
        if (amount != lastFluidAmount) {
            lastFluidAmount = amount;
            boilTick = 0;
            return;
        }
        if (isBoiling()) {
            boilTick++;
            if (boilTick >= 800) {
                setFluid(new FluidStack(IMFluids.HOT_WATER.getFlowing(), getTank().getFluidAmount()));
                setChanged();
            }
        } else {
            boilTick = 0;
        }
    }

    /**
     * 用于tick中，连接底部为热源，连接底部为热水就每秒消耗热水<br/>
     * 同时要防止多个蒸笼同时执行这个操作
     */
    public void tryDrainHotWater() {
        if (isWorking()) {
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

    public boolean hasWater() {
        return getTank().getFluid().getFluid().getFluidType().equals(Fluids.WATER.getFluidType());
    }

    public boolean hasHotWater() {
        return getTank().getFluid().getFluid().getFluidType().equals(IMFluids.HOT_WATER.getType());
    }

    public boolean hasHeatSource() {
        if (getLevel() == null) return false;
        return FarmerUtil.isHeatSource(getLevel().getBlockState(getBlockPos().below()));
    }

    public boolean isBoiling() {
        return hasHeatSource() && hasWater();
    }

    public boolean isWorking() {
        return hasHeatSource() && hasHotWater();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt(NBTList.DRAIN_TICK, drainTick);
        nbt.putInt(NBTList.BOIL_TICK, boilTick);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        drainTick = nbt.getInt(NBTList.DRAIN_TICK);
        boilTick = nbt.getInt(NBTList.BOIL_TICK);
    }

}
