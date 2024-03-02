package cn.solarmoon.immersive_delight.common.block_entity.base;

import cn.solarmoon.solarmoon_core.common.block_entity.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractServicePlateBlockEntity extends BaseContainerBlockEntity {

    private final ItemStackHandler inventory;

    public AbstractServicePlateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, 64, 1, pos, state);
        this.inventory = new ItemStackHandler(64) {
            public int getSlotLimit(int slot) {
                return 1;
            }
            public boolean isItemValid(int slot, ItemStack stack)
            {
                return stack.isEdible();
            }
        };
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    /**
     * @return 获取最后一个物品
     */
    public ItemStack getLastItem() {
        for (int i = 0; i < getInventory().getSlots(); i++) {
            ItemStack last = getInventory().getStackInSlot(getInventory().getSlots() - 1 - i);
            if (!last.isEmpty()) {
                return last;
            }
        }
        return ItemStack.EMPTY;
    }

}
