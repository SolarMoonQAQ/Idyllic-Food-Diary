package cn.solarmoon.immersive_delight.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ContainerHelper {

    /**
     * 保存单个物品到单物品容器物品tag内
     * @param stack 要设置的容器
     * @param set 设置的物品
     */
    public static void setItem(ItemStack stack, ItemStack set) {
        stack.getOrCreateTag().put("Item", set.save(new CompoundTag()));
    }

    /**
     * 保存单个物品到单方块容器物品tag内
     * @param blockEntity 要设置的容器
     * @param set 设置的物品
     */
    public static void setItem(BlockEntity blockEntity, ItemStack set) {
        blockEntity.getPersistentData().put("Item", set.save(new CompoundTag()));
    }

}
