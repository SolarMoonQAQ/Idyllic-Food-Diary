package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;

public class SteamerInventory extends ItemStackHandler {

    public static final String SECOND_INV = "SecondInventory";
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
    public boolean isItemValid(int slot, ItemStack stack) {
        boolean isStackDouble = Block.byItem(stack.getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null;
        if (!secondInv) {
            if (hasDouble(1)) return false; //有双方块物品则立刻占满此层槽位
            if (isStackDouble) return isInvEmpty(1); //输入为双方块物品则需要此层全空
            return slot < 4;
        } else {
            for (int i = 1; i < 3; i++) {
                if (hasDouble(i)) continue; //有双方块物品则立刻占满此层槽位
                if (isStackDouble) {
                    if (isInvEmpty(i)) return getValidSlotInLayer(i, slot); //输入为双方块物品则需要此层全空
                }
                else if (!hasDouble(i)) {
                    if (getValidSlotInLayer(i, slot)) return true;
                }
            }
            return false;
        }
    }

    public boolean getValidSlotInLayer(int layer, int slot) {
        if (layer == 1) {
            return slot < 4;
        } else return slot >= 4;
    }

    public boolean hasDouble(int layer) {
        if (layer == 1) {
            for (int i = 0; i < 4; i++) {
                if (Block.byItem(getStackInSlot(i).getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null) return true;
            }
            return false;
        } else if (layer == 2) {
            for (int i = 4; i < 8; i++) {
                if (Block.byItem(getStackInSlot(i).getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null) return true;
            }
            return false;
        }
        throw new RuntimeException("Layer " + layer + " is not existed.");
    }

    public ItemStack getDoubleStack(int layer) {
        if (layer == 1) {
            for (int i = 0; i < 4; i++) {
                if (Block.byItem(getStackInSlot(i).getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null) return getStackInSlot(i);
            }
            return ItemStack.EMPTY;
        } else if (layer == 2) {
            for (int i = 4; i < 8; i++) {
                if (Block.byItem(getStackInSlot(i).getItem()).defaultBlockState().getValues().get(IBedPartBlock.PART) != null) return getStackInSlot(i);
            }
            return ItemStack.EMPTY;
        }
        throw new RuntimeException("Layer " + layer + " is not existed.");
    }

    /**
     * @return 第x层是否为全空
     */
    public boolean isInvEmpty(int layer) {
        if (layer == 1) {
            for (int i = 0; i < 4; i++) {
                if (!getStackInSlot(i).isEmpty()) return false;
            }
            return true;
        } else if (layer == 2) {
            for (int i = 4; i < 8; i++) {
                if (!getStackInSlot(i).isEmpty()) return false;
            }
            return true;
        }
        throw new RuntimeException("Layer " + layer + " is not existed.");
    }

    //以防万一对nbt进行存储
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putBoolean(SECOND_INV, secondInv);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        secondInv = nbt.getBoolean(SECOND_INV);
    }

}
