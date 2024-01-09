package cn.solarmoon.immersive_delight.common.items.abstract_items;

import cn.solarmoon.immersive_delight.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.util.FluidHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseTankItem extends BlockItem {

    public BaseTankItem(Block block, Properties properties) {
        super(block, properties
                .stacksTo(1)
        );
    }

    /**
     * 获取最大容量
     */
    public int getMaxCapacity() {
        return 8000;
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
     * 检查物品内液体是否大于0
     */
    public boolean remainFluid(ItemStack stack) {
        return !FluidHelper.getFluidStack(stack).isEmpty();
    }

    /**
     * 让物品根据所装溶液动态改变显示名称
     */
    @Override
    public Component getName(ItemStack stack) {
        FluidStack fluidStack = FluidHelper.getFluidStack(stack);
        int fluidAmount = fluidStack.getAmount();
        String fluid = fluidStack.getFluid().getFluidType().getDescription().getString();
        if(fluidAmount != 0) return Component.translatable(stack.getDescriptionId() + "_with_fluid", fluid);
        return super.getName(stack);
    }

}
