package cn.solarmoon.idyllic_food_diary.compat.jade;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlockEntity;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPlateable;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.IBakingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.fermentation.IFermentationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling.IFoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.ingredient_handling.IIngredientHandlingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production.ITeaProductionRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CommonCookwareProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final String configId;

    public CommonCookwareProvider(String configId) {
        this.configId = configId;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity be = blockAccessor.getBlockEntity();
        if (be instanceof IStirFryRecipe stir) {
            JadeUtil.addStirFryStage(iTooltip, stir);
            JadeUtil.addByTime(stir.getStirFryTime(), stir.getStirFryRecipeTime(), iTooltip);
        }
        if (be instanceof IStewRecipe stew) {
            JadeUtil.addByTime(stew.getStewTime(), stew.getStewRecipeTime(), iTooltip);
        }
        if (be instanceof ISoupRecipe soup) {
            JadeUtil.addByTime(soup.getSimmerTime(), soup.getSimmerRecipeTime(), iTooltip);
        }
        if (be instanceof IWaterBoilingRecipe boil) {
            JadeUtil.addByTime(boil.getBoilTime(), boil.getBoilRecipeTime(), iTooltip);
        }
        if (be instanceof IFoodBoilingRecipe fBoil) {
            JadeUtil.addByTimeArray(fBoil.getFBTimes(), fBoil.getFBRecipeTimes(), fBoil.getInventory(), iTooltip);
        }
        if (be instanceof IBrewingRecipe brew) {
            JadeUtil.addByTime(brew.getBrewTime(), brew.getBrewRecipeTime(), iTooltip);
        }
        if (be instanceof IPlateable plate) {
            JadeUtil.addPlatingResult(iTooltip, plate);
        }
        if (be instanceof IIngredientHandlingRecipe ih) {
            JadeUtil.addIngredientHandlingResult(iTooltip, ih);
        }
        if (be instanceof SteamerBlockEntity steamer) {
            blockAccessor.getServerData().remove("JadeItemStorage"); // 阻止原来的容器显示
            JadeUtil.addSteamingTip(iTooltip, steamer);
            JadeUtil.addByTimeArray(steamer.getSteamTimes(), steamer.getSteamRecipeTimes(), steamer.getInvList(), iTooltip);
        }
        if (be instanceof ITeaProductionRecipe tp) {
            JadeUtil.addByTimeArray(tp.getTeaPrdTimes(), tp.getTeaPrdRecipeTimes(), tp.getInventory(), iTooltip);
        }
        if (be instanceof IBakingRecipe bake) {
            JadeUtil.addByTime(bake.getBakeTime(), bake.getBakeRecipeTime(), iTooltip);
        }
        if (be instanceof IEvaporationRecipe eva) {
            JadeUtil.addSteamingBaseTip(iTooltip, eva);
        }
        if (be instanceof IFermentationRecipe f) {
            JadeUtil.addByTime(f.getFermentTime(), f.getFermentRecipeTime(), iTooltip);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {

    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(IdyllicFoodDiary.MOD_ID, configId);
    }

}
