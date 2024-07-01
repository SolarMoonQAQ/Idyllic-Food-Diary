package cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grill.IGrillRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 拥有七个槽位，前六个限量1，可放任意物品，最后一个只能存入煤炭类物品，限量64
 */
public class GrillBlockEntity extends SyncedBlockEntity implements IGrillRecipe {

    private int[] times;
    private int[] recipeTimes;
    private final TileInventory inventory;
    private int burnTime;
    public int saveBurnTime;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.GRILL.get(), pos, state);
        this.times = new int[64];
        this.recipeTimes = new int[64];
        this.inventory = new TileInventory(7, this) {
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

    @Override
    public ItemStack getFuel() {
        return getInventory().getStackInSlot(6);
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    @Override
    public int getFuelTime() {
        return saveBurnTime;
    }

    @Override
    public void setFuelTime(int time) {
        saveBurnTime = time;
    }

    @Override
    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public int[] getGrillTimes() {
        return times;
    }

    @Override
    public int[] getGrillRecipeTimes() {
        return recipeTimes;
    }

    @Override
    public void setGrillTimes(int[] ints) {
        times = ints;
    }

    @Override
    public void setGrillRecipeTimes(int[] ints) {
        recipeTimes = ints;
    }

}
