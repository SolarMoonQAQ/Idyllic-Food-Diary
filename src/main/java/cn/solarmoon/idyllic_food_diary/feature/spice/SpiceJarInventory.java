package cn.solarmoon.idyllic_food_diary.feature.spice;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class SpiceJarInventory extends TileInventory {

    public SpiceJarInventory(BlockEntity blockEntity) {
        super(blockEntity);
    }

    public SpiceJarInventory(int size, BlockEntity blockEntity) {
        super(size, blockEntity);
    }

    public SpiceJarInventory(int size, int slotLimit, BlockEntity blockEntity) {
        super(size, slotLimit, blockEntity);
    }

    public SpiceJarInventory(NonNullList<ItemStack> stacks, BlockEntity blockEntity) {
        super(stacks, blockEntity);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(IMItemTags.SPICE);
    }

}
