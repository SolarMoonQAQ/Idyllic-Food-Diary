package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.steaming;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerInventoryList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Optional;

public interface ISteamingRecipe {

    String STEAMER_INV_LIST = "SteamerInventoryList";
    String STEAMER_TIME = "SteamerTime";
    String STEAMER_RECIPE_TIME = "SteamerRecipeTime";

    default BlockEntity sb() {
        return (BlockEntity) this;
    }

    /**
     * 配方匹配，连接底部有正在工作的基座即通过
     */
    default Optional<SteamingRecipe> getSteamingRecipe(int index) {
        Level level = sb().getLevel();
        if (level == null) return Optional.empty();
        List<SteamingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.STEAMING.get());
        ItemStack stack = getInvList().getStackInSlot(index);
        return recipes.stream().filter(recipe -> recipe.input().test(stack) && canWork()).findFirst();
    }

    default boolean trySteam() {
        boolean flag = false;
        Level level = sb().getLevel();
        if (level == null) return false;
        if (level.isClientSide) return false;
        for (int i = 0; i < getInvList().getSlots(); i++) {
            Optional<SteamingRecipe> recipeOp = getSteamingRecipe(i);
            if (recipeOp.isPresent()) {
                SteamingRecipe recipe = recipeOp.get();
                getSteamRecipeTimes()[i] = recipe.time();
                getSteamTimes()[i] = getSteamTimes()[i] + 1;
                if (getSteamTimes()[i] >= recipe.time()) {
                    getInvList().setStackInSlot(i, recipe.output().copy());
                    getSteamTimes()[i] = 0;
                    getSteamRecipeTimes()[i] = 0;
                    sb().setChanged();
                }
                flag = true;
            } else {
                getSteamTimes()[i] = 0;
                getSteamRecipeTimes()[i] = 0;
            }
        }
        return flag;
    }

    SteamerInventoryList getInvList();

    boolean canWork();

    int[] getSteamTimes();

    int[] getSteamRecipeTimes();

    void setSteamTimes(int[] var1);

    void setSteamRecipeTimes(int[] var1);

}
