package cn.solarmoon.immersive_delight.api.util;

import cn.solarmoon.immersive_delight.common.IMFluids;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * 主要用于对流体tank类进行处理
 */
public class FluidHelper {

    /**
     * 获取物品容器内的fluidTank
     */
    public static IFluidHandlerItem getTank(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
    }

    /**
     * 获取方块实体容器内的fluidTank
     */
    public static IFluidHandler getTank(BlockEntity blockEntity) {
        return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
    }

    /**
     * 获取物品容器内的fluidStack
     */
    public static FluidStack getFluidStack(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        return tankStack.getFluidInTank(0);
    }

    /**
     * 获取方块实体容器内的fluidStack
     */
    public static FluidStack getFluidStack(BlockEntity blockEntity) {
        IFluidHandler tank = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
        return tank.getFluidInTank(0);
    }

    /**
     * 根据液体容量获取大小百分比
     */
    public static float getScale(FluidTank tank) {
        int stored = tank.getFluidAmount();
        int capacity = tank.getCapacity();
        return (float) stored / capacity;
    }

    /**
     * 根据液体容量获取大小百分比
     */
    public static float getScale(IFluidHandlerItem tank) {
        int stored = tank.getFluidInTank(0).getAmount();
        int capacity = tank.getTankCapacity(0);
        return (float) stored / capacity;
    }

    /**
     * 用于强制设置物品里的液体（前者是被设置的，后者是设置的内容）
     */
    public static void setTank(ItemStack stack, BlockEntity blockEntity) {
        //把blockEntity的tank注入item
        IFluidHandlerItem tankStack = getTank(stack);
        FluidStack fluidStack = getFluidStack(blockEntity);
        tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        tankStack.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }

    /**
     * 用于强制设置方块实体里的液体（前者是被设置的，后者是设置的内容）
     */
    public static void setTank(BlockEntity blockEntity, ItemStack stack) {
        //从stack注入blockEntity
        IFluidHandler tank = getTank(blockEntity);
        FluidStack fluidStack = getFluidStack(stack);
        tank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
    }

    /**
     * 检查两个流体栈是否完全匹配（包括数量）
     */
    public static boolean isMatch(FluidStack fluid1, FluidStack fluid2) {
        return fluid1.equals(fluid2) && fluid1.getAmount() == fluid2.getAmount();
    }

    /**
     * 检查是否还能放入液体
     * 规则为已有液体必须相匹配，且剩余空间大于等于要放入的液体（或者为空）
     * 相反的检查可以用contains
     */
    public static boolean canStillPut(FluidTank tank, FluidStack fluidStack) {
        int remain = tank.getCapacity() - tank.getFluidAmount();
        int put = fluidStack.getAmount();
        boolean match = tank.getFluid().equals(fluidStack);
        return remain >= put && (match || tank.getFluid().isEmpty());
    }

    /**
     * 用于判断输入值是否与任一模组的液体匹配
     */
    public static boolean IMFluidsMatch(FluidType fluidType) {
        for(var type : IMFluids.FluidTypes) {
            if (type.equals(fluidType)) return true;
        }
        return false;
    }

    /**
     * 用于判断实体是否在任一热水中
     */
    public static boolean isInHotFluid(Entity entity) {
        for(var type : IMFluids.HotFluidTypes) {
            if (entity.isInFluidType(type)) return true;
        }
        return false;
    }

    /**
     * 用于判断实体是否在任一可生成粒子效果气泡的水中
     */
    public static boolean isInFluid(Entity entity) {
        for(var type : IMFluids.FluidTypes) {
            if (entity.isInFluidType(type)) return true;
        }
        return false;
    }

}
