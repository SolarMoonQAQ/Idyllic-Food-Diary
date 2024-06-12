package cn.solarmoon.idyllic_food_diary.element.matter.cookware.stove;

import cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature.IInlineBlockMethodCall;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class StoveInventory extends ItemStackHandler {

    private final StoveBlockEntity stove;

    public StoveInventory(StoveBlockEntity stove) {
        super(2);
        this.stove = stove;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 1) return stack.is(ItemTags.LOGS) || stack.is(ItemTags.PLANKS);
        return Block.byItem(stack.getItem()) instanceof IInlineBlockMethodCall;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (slot == 0) stove.updatePot();
    }

}
