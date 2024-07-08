package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerBlockEntity extends SyncedBlockEntity implements IContainerTile {

    private final TileInventory inventory;

    public ContainerBlockEntity(int size, BlockPos pos, BlockState state) {
        super(IMBlockEntities.CONTAINER.get(), pos, state);
        this.inventory = new TileInventory(size, 1, this) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack)
            {
                return stack.isEdible();
            }
        };
    }

    public ContainerBlockEntity(BlockEntityType<?> type, int size, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new TileInventory(size, 1, this) {
            @Override
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
