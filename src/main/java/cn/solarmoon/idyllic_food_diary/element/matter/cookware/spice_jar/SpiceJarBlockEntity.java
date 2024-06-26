package cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SpiceJarBlockEntity extends BlockEntity implements IContainerTile {

    private final TileInventory inventory;

    public SpiceJarBlockEntity(int slotSize, BlockPos pos, BlockState state) {
        super(IMBlockEntities.SPICE_JAR.get(), pos, state);
        inventory = new TileInventory(1, slotSize, this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return stack.is(IMItemTags.SPICE);
            }
        };
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
