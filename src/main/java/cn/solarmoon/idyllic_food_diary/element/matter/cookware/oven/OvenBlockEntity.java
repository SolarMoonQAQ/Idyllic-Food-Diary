package cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven;

import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.BakingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.IBakingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class OvenBlockEntity extends SyncedBlockEntity implements IBakingRecipe {

    private final TileInventory inv;
    private int time;
    private int recipeTime;
    private final SpiceList spices;

    public OvenBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.OVEN.get(), pos, state);
        inv = new TileInventory(1, 1, this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                List<BakingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.BAKING.get());
                return recipes.stream()
                        .anyMatch(recipe -> recipe.ingredient().test(stack));
            }
        };
        spices = new SpiceList();
    }

    public BlockPos getTopChimney() {
        BlockPos pos = getBlockPos().above();
        Level level = getLevel();
        if (level == null) return pos;
        BlockState chimney = level.getBlockState(pos);
        while (chimney.is(IMBlockTags.CHIMNEY)) {
            pos = pos.above();
            chimney = level.getBlockState(pos);
        }
        return pos;
    }

    @Override
    public ItemStackHandler getInventory() {
        return inv;
    }

    @Override
    public int getBakeTime() {
        return time;
    }

    @Override
    public int getBakeRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setBakeTime(int time) {
        this.time = time;
    }

    @Override
    public void setBakeRecipeTime(int time) {
        this.recipeTime = time;
    }

    @Override
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.EMPTY;
    }

    @Override
    public boolean timeToResetSpices() {
        return findBakingRecipe().isEmpty();
    }

}
