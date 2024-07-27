package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.ingredient_handling.IIngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CuttingBoardBlockEntity extends SyncedBlockEntity implements IIngredientHandlingRecipe {

    public SpiceList spices = new SpiceList();
    private final TileInventory inventory;

    public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.CUTTING_BOARD.get(), pos, state);
        inventory = new TileInventory(9, 1, this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !stack.is(IMItemTags.CONTAINER);
            }
        };
    }

    @Override
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.ADD;
    }

    @Override
    public boolean timeToResetSpices() {
        return findHandleRecipe().isEmpty();
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
