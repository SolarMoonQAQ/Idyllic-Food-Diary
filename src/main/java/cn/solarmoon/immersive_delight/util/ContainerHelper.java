package cn.solarmoon.immersive_delight.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.swing.plaf.basic.BasicComboBoxUI;

public class ContainerHelper {

    /**
     * 把方块实体物品信息存入item
     */
    public static void setInventory(ItemStack stack, BlockEntity blockEntity) {
        ItemStackHandler inventory = (ItemStackHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        stack.getOrCreateTag().put("inventory", inventory.serializeNBT());
    }

    /**
     * 把item物品信息存入方块实体
     */
    public static void setInventory(BlockEntity blockEntity, ItemStack stack) {
        blockEntity.getPersistentData().put("inventory", stack.getOrCreateTag().getCompound("inventory"));
    }

    /**
     * 获取物品的inventory信息
     */
    public static ItemStackHandler getInventory(ItemStack stack) {
        ItemStackHandler stackHandler = new ItemStackHandler();
        stackHandler.deserializeNBT(stack.getOrCreateTag().getCompound("inventory"));
        return stackHandler;
    }

}
