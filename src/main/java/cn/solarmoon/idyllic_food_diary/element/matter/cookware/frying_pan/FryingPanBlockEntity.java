package cn.solarmoon.idyllic_food_diary.element.matter.cookware.frying_pan;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_base.BaseTCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FryingPanBlockEntity extends BaseTCBlockEntity implements IStirFryRecipe {

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

    public FryingPanBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.FRYING_PAN.get(), 250, 9, 1, pos, state);
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

}
