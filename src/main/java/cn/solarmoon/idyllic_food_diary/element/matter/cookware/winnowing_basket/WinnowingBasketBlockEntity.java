package cn.solarmoon.idyllic_food_diary.element.matter.cookware.winnowing_basket;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production.ITeaProductionRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class WinnowingBasketBlockEntity extends SyncedBlockEntity implements ITeaProductionRecipe {

    private final TileInventory inventory;
    private int[] times = new int[64];
    private int[] recipeTimes = new int[64];

    public WinnowingBasketBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.WINNOWING_BASKET.get(), pos, state);
        inventory = new TileInventory(4, 1, this);
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public int[] getTeaPrdTimes() {
        return times;
    }

    @Override
    public int[] getTeaPrdRecipeTimes() {
        return recipeTimes;
    }

    @Override
    public void setTeaPrdTimes(int[] ints) {
        times = ints;
    }

    @Override
    public void setTeaPrdRecipeTimes(int[] ints) {
        recipeTimes = ints;
    }

}
