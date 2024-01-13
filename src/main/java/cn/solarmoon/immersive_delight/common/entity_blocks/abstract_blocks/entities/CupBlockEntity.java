package cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 所有茶杯类的基本类
 * 除去tank外，还能存储一个物品
 */
public abstract class CupBlockEntity extends TankBlockEntity {

    public ItemStackHandler inventory;

    public CupBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        //一格，一个物品
        this.inventory = new ItemStackHandler(1) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("inventory"));
    }

    /**
     * 既有储液能力，又是物品容器
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> inventory).cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * 插入容纳的物品（仅1个）
     * 会返回计算消耗后的物品栈
     * 不要再用shrink！
     */
    public ItemStack insertItem(ItemStack itemStack) {
        ItemStack stack = inventory.insertItem(0, itemStack, false);
        setChanged();
        return stack;
    }

    /**
     * 获取其中的stack
     */
    public ItemStack getItem() {
        return inventory.getStackInSlot(0);
    }

    /**
     * 从中提取物品
     */
    public ItemStack extractItem() {
        ItemStack stack = inventory.extractItem(0, Integer.MAX_VALUE, false);
        setChanged();
        return stack;
    }

    /**
     * 强制设置槽位的物品
     */
    public void setItem(ItemStack stack) {
        inventory.setStackInSlot(0, stack);
        setChanged();
    }

    /**
     * 从stack中读取inventory信息
     */
    public void setInventory(ItemStack stack) {
        inventory.deserializeNBT(stack.getOrCreateTag().getCompound("inventory"));
    }



}
