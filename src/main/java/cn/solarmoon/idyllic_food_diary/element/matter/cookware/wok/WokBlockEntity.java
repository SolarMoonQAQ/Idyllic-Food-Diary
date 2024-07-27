package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.ITempChanger;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import cn.solarmoon.solarmoon_core.api.tile.fluid.TileTank;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WokBlockEntity extends SyncedBlockEntity implements IStirFryRecipe, ITempChanger {

    private final TileInventory inventory;
    private final TileTank fluidTank;

    private int exp;
    private ItemStack result = ItemStack.EMPTY;
    private Ingredient container = Ingredient.EMPTY;
    private StirFryRecipe stirFryRecipe;
    private int presentStage;
    private int stirFryTime;
    private int stirFryRecipeTime;
    private boolean canStirFry;
    private int fryCount;
    private ItemStack pendingItem = ItemStack.EMPTY;
    private final SpiceList spices = new SpiceList();

    public int soundTick;

    public WokBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.WOK.get(), pos, state);
        inventory = new TileInventory(6, 1, this) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !(Block.byItem(stack.getItem()) instanceof CookwareBlock);
            }
        };
        fluidTank = new TileTank(250, this);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(3);
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public Ingredient getContainer() {
        return container;
    }

    @Override
    public void setResult(ItemStack newResult) {
        result = newResult;
    }

    @Override
    public void setContainer(Ingredient newContainer) {
        container = newContainer;
    }

    @Override
    public @Nullable StirFryRecipe getStirFryRecipe() {
        return stirFryRecipe;
    }

    @Override
    public void setStirFryRecipe(StirFryRecipe recipe) {
        stirFryRecipe = recipe;
    }

    @Override
    public int getPresentStage() {
        return presentStage;
    }

    @Override
    public void setPresentStage(int stage) {
        presentStage = stage;
    }

    @Override
    public int getStirFryTime() {
        return stirFryTime;
    }

    @Override
    public void setStirFryTime(int time) {
        stirFryTime = time;
    }

    @Override
    public int getStirFryRecipeTime() {
        return stirFryRecipeTime;
    }

    @Override
    public void setStirFryRecipeTime(int time) {
        stirFryRecipeTime = time;
    }

    @Override
    public boolean canStirFry() {
        return canStirFry;
    }

    @Override
    public void setCanStirFry(boolean or) {
        canStirFry = or;
    }

    @Override
    public int getFryCount() {
        return fryCount;
    }

    @Override
    public void setFryCount(int count) {
        fryCount = count;
    }

    @Override
    public ItemStack getPendingItem() {
        return pendingItem;
    }

    @Override
    public void setPendingItem(ItemStack stack) {
        pendingItem = stack;
    }

    @Override
    public SpiceList getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.MIX;
    }

    @Override
    public boolean timeToResetSpices() {
        return !isStirFrying();
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

}
