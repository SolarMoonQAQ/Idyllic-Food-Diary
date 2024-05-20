package cn.solarmoon.idyllic_food_diary.core.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ICookingPotRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISpiceable;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.CookingPotRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.api.common.block_entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookingPotBlockEntity extends BaseTCBlockEntity implements ICookingPotRecipe, IKettleRecipe, IBlockEntityAnimateTicker {

    private int time;
    private int recipeTime;

    private int animTick;
    private float last;

    private ItemStack pendingResult = ItemStack.EMPTY;
    private Ingredient container = Ingredient.EMPTY;

    private final List<Spice> spices = new ArrayList<>();

    public int boilTime;
    public int boilRecipeTime;

    public CookingPotBlockEntity(int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(IMBlockEntities.SOUP_POT.get(), maxCapacity, size, slotLimit, pos, state);
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public int getRecipeTime() {
        return recipeTime;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void setRecipeTime(int recipeTime) {
        this.recipeTime = recipeTime;
    }

    @Override
    public int getTicks() {
        return animTick;
    }

    @Override
    public void setTicks(int ticks) {
        animTick = ticks;
    }

    @Override
    public float getLast() {
        return last;
    }

    @Override
    public void setLast(float last) {
        this.last = last;
    }

    @Override
    public int getBoilRecipeTime() {
        return boilRecipeTime;
    }

    @Override
    public int getBoilTime() {
        return boilTime;
    }

    @Override
    public void setBoilRecipeTime(int time) {
        boilRecipeTime = time;
    }

    @Override
    public void setBoilTime(int time) {
        boilTime = time;
    }

    @Override
    public List<Spice> getSpices() {
        return spices;
    }

    @Override
    public Spice.Step getSpiceStep() {
        return Spice.Step.MIX;
    }

    @Override
    public boolean timeToResetSpices() {
        return getCheckedRecipe().isEmpty();
    }

    @Override
    public ItemStack getResult() {
        return pendingResult;
    }

    @Override
    public Ingredient getContainer() {
        return container;
    }

    @Override
    public void setResult(ItemStack newResult) {
        pendingResult = newResult;
    }

    @Override
    public void setContainer(Ingredient newContainer) {
        container = newContainer;
    }

}
