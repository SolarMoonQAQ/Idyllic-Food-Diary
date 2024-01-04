package cn.solarmoon.immersive_delight.common.blocks.abstract_blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;


/**
 * 基本的储罐类抽象实体
 * 没有多余的功能，只是添加一个储罐
 * 可以与大部分液体类工具/方块交互
 */
public abstract class TankBlockEntity extends BlockEntity {

    public FluidTank tank;

    public static final String NBT_FLUID = "fluid";

    public TankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tank = new FluidTank(getTankMaxVolume());
    }

    /**
     * 设置forge提供的储罐系统
     * @param cap The capability to check
     * @param side The Side to check from,
     *   <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(() -> tank).cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * @return 设置储罐最大容积
     */
    public int getTankMaxVolume() {
        return 8000;
    }

    /**
     * 一个强制设置储罐内容物的方法
     */
    public void setFluid(FluidStack fluidStack) {
        tank.setFluid(fluidStack);
    }

    /**
     * 配合setChanged调用，用以同步各类信息（主要是tag信息）
     * @param tag 读取save的tag
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag fluid = tag.getCompound(NBT_FLUID);
        tank.readFromNBT(fluid);
    }

    /**
     * 配合setChanged调用，用以同步各类信息（主要是tag信息）
     * @param tag 自定义以输入load
     */
    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag fluid = new CompoundTag();
        tank.writeToNBT(fluid);
        tag.put(NBT_FLUID, fluid);
    }

}
