package cn.solarmoon.immersive_delight.common.block_entity.inventoryHandler;

import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SteamerInventory extends ItemStackHandler {

    public boolean secondInv;

    public SteamerInventory() {
        super(8);
        this.secondInv = false;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot > 3 && secondInv) {
            return true;
        }
        return slot <= 3;
    }

    //以防万一对nbt进行存储
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putBoolean(NBTList.SECOND_INV, secondInv);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        secondInv = nbt.getBoolean(NBTList.SECOND_INV);
    }

}
