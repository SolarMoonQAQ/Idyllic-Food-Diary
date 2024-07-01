package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grill;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ISimpleFuelBlockEntity;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;

public interface IGrillRecipe extends IContainerTile, ISimpleFuelBlockEntity {

    String GRILL_TIME = "GrillTime";
    String GRILL_RECIPE_TIME = "GrillRecipeTime";

    default boolean tryCook() {
        ItemStackHandler inv = getInventory();
        BlockPos pos = sf().getBlockPos();
        Level level = sf().getLevel();
        if (level == null) return false;
        if (level.isClientSide) return false;
        boolean flag = false;
        for (int i = 0; i < inv.getSlots(); i++) {
            Optional<CampfireCookingRecipe> recipeOp = findGrillRecipe(i);
            if (recipeOp.isPresent()) {
                CampfireCookingRecipe recipe = recipeOp.get();
                getGrillRecipeTimes()[i] = recipe.getCookingTime() / 3;
                getGrillTimes()[i] = getGrillTimes()[i]+ 1;
                if (getGrillTimes()[i] >= recipe.getCookingTime() / 3) {
                    ItemStack out = recipe.getResultItem(level.registryAccess());
                    inv.setStackInSlot(i, out);
                    getGrillTimes()[i] = 0;
                    sf().setChanged();
                }
                flag = true;
            } else {
                getGrillTimes()[i] = 0;
                getGrillRecipeTimes()[i] = 0;
            }
        }
        return flag;
    }

    default Optional<CampfireCookingRecipe> findGrillRecipe(int index) {
        Level level = sf().getLevel();
        if (level == null) return Optional.empty();
        BlockState state = sf().getBlockState();
        List<CampfireCookingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CAMPFIRE_COOKING);
        ItemStack stack = getInventory().getStackInSlot(index);
        return recipes.stream().filter(recipe ->
                recipe.getIngredients().get(0).test(stack) && state.getValue(ILitBlock.LIT)).findFirst();
    }

    int[] getGrillTimes();
    int[] getGrillRecipeTimes();
    void setGrillTimes(int[] ints);
    void setGrillRecipeTimes(int[] ints);

}
