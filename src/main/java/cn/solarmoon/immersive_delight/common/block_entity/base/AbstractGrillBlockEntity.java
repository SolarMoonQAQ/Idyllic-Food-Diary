package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.immersive_delight.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 拥有七个槽位，前六个限量1，可放任意物品，最后一个只能存入煤炭类物品，限量64
 */
public class AbstractGrillBlockEntity extends BlockEntity implements IContainerBlockEntity {

    private int[] times;
    private final ItemStackHandler inventory;
    private int burnTime;
    public int saveBurnTime;

    public AbstractGrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.times = new int[64];
        this.inventory = new ItemStackHandler(7) {
            @Override
            public int getSlotLimit(int slot) {
                if (slot < 6) {
                    return 1;
                }
                return 64;
            }
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if (slot < 6 && !stack.is(ItemTags.COALS)) {
                    return true;
                } else return slot >= 6 && stack.is(ItemTags.COALS);
            }
        };
    }

    public boolean noFuel() {
        return !inventory.getStackInSlot(6).is(ItemTags.COALS);
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] ints) {
        times = ints;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putIntArray(NBTList.SINGLE_STACK_TIME, getTimes());
        nbt.putInt(NBTList.BURNING_TIME, getBurnTime());
        nbt.putInt(NBTList.BURNING_TIME_SAVING, saveBurnTime);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        int[] getFrom = nbt.getIntArray(NBTList.SINGLE_STACK_TIME);
        if (getFrom.length != 0) {
            setTimes(getFrom);
        }
        setBurnTime(nbt.getInt(NBTList.BURNING_TIME));
        saveBurnTime = nbt.getInt(NBTList.BURNING_TIME_SAVING);
    }

}
