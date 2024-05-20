package cn.solarmoon.idyllic_food_diary.api.common.block_entity.inventory;

import cn.solarmoon.idyllic_food_diary.core.data.tags.IMItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SpiceJarInventory extends ItemStackHandler {

    private int slotSize;

    public SpiceJarInventory(int slotSize) {
        super(1);
        this.slotSize = slotSize;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotSize;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.is(IMItemTags.SPICE);
    }

}
