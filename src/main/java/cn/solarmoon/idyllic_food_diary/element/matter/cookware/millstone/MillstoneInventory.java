package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grinding.IGrindingRecipe;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class MillstoneInventory extends TileInventory {

    public MillstoneInventory(int size, BlockEntity blockEntity) {
        super(size, blockEntity);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 0) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    /**
     * 阻止一般情况下插入输出槽<br/>
     * 用下面那个插入
     */
    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot != 0) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack realInsert(int slot, @NotNull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack realExtract(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 0) return ((IGrindingRecipe) getBlockEntity()).findGrindingRecipe(stack).isPresent();
        return super.isItemValid(slot, stack);
    }
}
