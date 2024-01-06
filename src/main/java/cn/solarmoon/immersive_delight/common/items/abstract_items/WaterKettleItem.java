package cn.solarmoon.immersive_delight.common.items.abstract_items;

import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WaterKettleItem extends BlockItem {

    public WaterKettleItem(Block block, Properties properties) {
        super(block, properties
                .stacksTo(1)
        );
    }

    /**
     * 获取最大容量
     */
    public int getMaxCapacity() {
        return 250;
    }

    /**
     * 使用时长
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    /**
     * 将其赋予一个容器
     * 不！能够实现与各类液体容器交互
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, getMaxCapacity());
    }

    /**
     * 让壶类物品显示其存储的液体信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        components.add(Component.literal(tankStack.getFluidInTank(0).getFluid().getFluidType().getDescription().getString() + tankStack.getFluidInTank(0).getAmount()));
    }

    /**
     * 让壶根据所装溶液动态改变显示名称
     */
    @Override
    public Component getName(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        FluidStack fluidStack = tankStack.getFluidInTank(0);
        int fluidAmount = fluidStack.getAmount();
        String fluid = fluidStack.getFluid().getFluidType().getDescription().getString();
        if(fluidAmount != 0) return Util.translation("block", "kettle_with_fluid", fluid);
        return super.getName(stack);
    }

    /**
     * 检查物品内液体是否大于0
     */
    public boolean remainFluid(ItemStack stack) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        return tankStack.getFluidInTank(0).getAmount() > 0;
    }

    /**
     * 同步流体信息，防止假右键操作
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int tick, boolean p_41408_) {
        IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(stack);
        ClientPackSerializer.sendPacket(entity.getOnPos(), stacks, tankStack.getFluidInTank(0), 0, "updateCupItem");
    }

}
