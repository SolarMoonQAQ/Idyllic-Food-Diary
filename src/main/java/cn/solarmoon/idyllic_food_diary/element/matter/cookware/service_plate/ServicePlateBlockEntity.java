package cn.solarmoon.idyllic_food_diary.element.matter.cookware.service_plate;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_base.BaseContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;

public class ServicePlateBlockEntity extends BaseContainerBlockEntity {

    private final ItemStackHandler inventory;

    public ServicePlateBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.PLATE.get(), 64, 1, pos, state);
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

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(3);
    }

}
