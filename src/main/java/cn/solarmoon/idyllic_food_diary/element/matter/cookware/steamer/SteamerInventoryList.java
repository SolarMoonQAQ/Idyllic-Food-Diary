package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SteamerInventoryList extends ArrayList<SteamerInventory> implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag> {

    private final SteamerBlockEntity steamer;

    public SteamerInventoryList(SteamerBlockEntity steamer) {
        this.steamer = steamer;
        add(steamer.newInventory());
    }

    /**
     * @return 获取所有的物品
     */
    public NonNullList<ItemStack> getAllStacks() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        forEach(inv -> stacks.addAll(inv.getStacks()));
        return stacks;
    }

    @Override
    public int getSlots() {
        return getAllStacks().size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return getAllStacks().get(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        int layer = slot / steamer.getMaxStack();
        for (int i = 0; i < steamer.newInventory().getSlots(); i++) {
            ItemStack s = get(layer).insertItem(i, stack, simulate);
            if (!s.equals(stack, false)) return s;
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        int layer = slot / steamer.getMaxStack();
        for (int i = 0; i < steamer.newInventory().getSlots(); i++) {
            ItemStack s = get(layer).extractItem(i, amount, simulate);
            if (!s.isEmpty()) return s;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public ListTag serializeNBT() {
        ListTag listTag = new ListTag();
        forEach(inv -> listTag.add(inv.serializeNBT()));
        return listTag;
    }

    @Override
    public void deserializeNBT(ListTag listTag) {
        clear();
        for (int i = 0; i < listTag.size(); i++) {
            SteamerInventory s = steamer.newInventory();
            s.deserializeNBT(listTag.getCompound(i));
            add(s);
        }
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        int layer = slot / steamer.getMaxStack();
        get(layer).setStackInSlot(slot % steamer.getMaxStack(), stack);
    }

}
