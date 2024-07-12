package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.element.matter.stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.baking.IBakingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.fermentation.IFermentationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.food_boiling.IFoodBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.grill.IGrillRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.steaming.ISteamingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.tea_production.ITeaProductionRecipe;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpicesCap;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.solarmoon_core.api.event.BlockEntityDataEvent;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;

public class BlockEntityDataHolderEvent {

    @SubscribeEvent
    public void save(BlockEntityDataEvent.Save event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IWaterBoilingRecipe kettle) {
            tag.putInt(IWaterBoilingRecipe.BOIL_TICK, kettle.getBoilTime());
        }
        if (be instanceof IBrewingRecipe brew) {
            tag.putInt(IBrewingRecipe.BREW_TICK, brew.getBrewTime());
        }
        if (be instanceof ISpiceable spiceable) {
            ListTag listTag = spiceable.getSpices().serializeNBT();
            tag.put(SpicesCap.SPICES, listTag);
        }
        if (be instanceof IPlateable pendingResult) {
            tag.put(IPlateable.RESULT, pendingResult.getResult().serializeNBT());
            tag.putString(IPlateable.CONTAINER, pendingResult.getContainer().toJson().toString());
        }
        if (be instanceof IExpGiver expGiver) {
            tag.putInt(IExpGiver.EXP, expGiver.getExp());
        }
        if (be instanceof IEvaporationRecipe eva) {
            tag.putInt(IEvaporationRecipe.DRAIN_TICK, eva.getEvaporationTick());
        }
        if (be instanceof IStewRecipe stew) {
            tag.putInt(IStewRecipe.STEW_TIME, stew.getStewTime());
            tag.putInt(IStewRecipe.STEW_RECIPE_TIME, stew.getStewRecipeTime());
        }
        if (be instanceof ISoupRecipe simmer) {
            tag.putInt(ISoupRecipe.SIMMER_TIME, simmer.getSimmerTime());
            tag.putInt(IStewRecipe.STEW_RECIPE_TIME, simmer.getSimmerRecipeTime());
        }
        if (be instanceof ISimpleFuelBlockEntity fuel) {
            tag.putInt(ISimpleFuelBlockEntity.BURNING_TIME, fuel.getBurnTime());
            tag.putInt(ISimpleFuelBlockEntity.BURNING_TIME_SAVING, fuel.getFuelTime());
        }
        if (be instanceof IStirFryRecipe stirFry) {
            tag.putInt(IStirFryRecipe.STIR_FRY_TIME, stirFry.getStirFryTime());
            tag.putInt(IStirFryRecipe.STIR_FRY_RECIPE_TIME, stirFry.getStirFryRecipeTime());
            tag.putInt(IStirFryRecipe.STIR_FRY_STAGE_NUM, stirFry.getPresentStage());
            tag.putInt(IStirFryRecipe.STIR_FRY_COUNT, stirFry.getFryCount());
            tag.putBoolean(IStirFryRecipe.STIR_FRY_CAN, stirFry.canStirFry());
            if (stirFry.getStirFryRecipe() != null) tag.putString(IStirFryRecipe.STIR_FRY_RECIPE, stirFry.getStirFryRecipe().id().toString());
            tag.put(IStirFryRecipe.STIR_FRY_PENDING, stirFry.getPendingItem().save(new CompoundTag()));
        }
        if (be instanceof ITeaProductionRecipe tp) {
            tag.putIntArray(ITeaProductionRecipe.TEA_PRODUCTION_TIME, tp.getTeaPrdTimes());
            tag.putIntArray(ITeaProductionRecipe.TEA_PRODUCTION_RECIPE_TIME, tp.getTeaPrdRecipeTimes());
        }
        if (be instanceof IGrillRecipe grill) {
            tag.putIntArray(IGrillRecipe.GRILL_TIME, grill.getGrillTimes());
            tag.putIntArray(IGrillRecipe.GRILL_RECIPE_TIME, grill.getGrillRecipeTimes());
        }
        if (be instanceof IFoodBoilingRecipe fb) {
            tag.putIntArray(IFoodBoilingRecipe.FB_TIME, fb.getFBTimes());
            tag.putIntArray(IFoodBoilingRecipe.FB_RECIPE_TIME, fb.getFBRecipeTimes());
        }
        if (be instanceof ISteamingRecipe steamer) {
            tag.putIntArray(ISteamingRecipe.STEAMER_TIME, steamer.getSteamTimes());
            tag.putIntArray(ISteamingRecipe.STEAMER_RECIPE_TIME, steamer.getSteamRecipeTimes());
            tag.put(ISteamingRecipe.STEAMER_INV_LIST, steamer.getInvList().serializeNBT());
        }
        if (be instanceof IBakingRecipe bake) {
            tag.putInt(IBakingRecipe.BAKE_TIME, bake.getBakeTime());
            tag.putInt(IBakingRecipe.BAKE_RECIPE_TIME, bake.getBakeRecipeTime());
        }
        if (be instanceof IFermentationRecipe f) {
            tag.putInt(IFermentationRecipe.FERMENT_TIME, f.getFermentTime());
            tag.putInt(IFermentationRecipe.FERMENT_RECIPE_TIME, f.getFermentRecipeTime());
        }
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        Level level = be.getLevel();
        if (be instanceof IWaterBoilingRecipe kettle) {
            kettle.setBoilTime(tag.getInt(IWaterBoilingRecipe.BOIL_TICK));
        }
        if (be instanceof IBrewingRecipe brew) {
            brew.setBrewTime(tag.getInt(IBrewingRecipe.BREW_TICK));
        }
        if (be instanceof ISpiceable spiceable) {
            spiceable.getSpices().deserializeNBT(tag.getList(SpicesCap.SPICES, ListTag.TAG_COMPOUND));
        }
        if (be instanceof IPlateable pendingResult) {
            pendingResult.setResult(ItemStack.of(tag.getCompound(IPlateable.RESULT)));
            if (tag.contains(IPlateable.CONTAINER)) pendingResult.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(IPlateable.CONTAINER))));
        }
        if (be instanceof IExpGiver expGiver) {
            expGiver.setExp(tag.getInt(IExpGiver.EXP));
        }
        if (be instanceof IEvaporationRecipe eva) {
            eva.setEvaporationTick(tag.getInt(IEvaporationRecipe.DRAIN_TICK));
        }
        if (be instanceof IStewRecipe stew) {
            stew.setStewTime(tag.getInt(IStewRecipe.STEW_TIME));
            stew.setStewRecipeTime(tag.getInt(IStewRecipe.STEW_RECIPE_TIME));
        }
        if (be instanceof ISoupRecipe simmer) {
            simmer.setSimmerTime(tag.getInt(ISoupRecipe.SIMMER_TIME));
            simmer.setSimmerRecipeTime(tag.getInt(ISoupRecipe.SIMMER_RECIPE_TIME));
        }
        if (be instanceof ISimpleFuelBlockEntity fuel) {
            fuel.setBurnTime(tag.getInt(ISimpleFuelBlockEntity.BURNING_TIME));
            fuel.setFuelTime(tag.getInt(ISimpleFuelBlockEntity.BURNING_TIME_SAVING));
        }
        if (be instanceof IStirFryRecipe stirFry) {
            stirFry.setStirFryTime(tag.getInt(IStirFryRecipe.STIR_FRY_TIME));
            stirFry.setStirFryRecipeTime(tag.getInt(IStirFryRecipe.STIR_FRY_RECIPE_TIME));
            stirFry.setFryCount(tag.getInt(IStirFryRecipe.STIR_FRY_COUNT));
            stirFry.setPresentStage(tag.getInt(IStirFryRecipe.STIR_FRY_STAGE_NUM));
            stirFry.setCanStirFry(tag.getBoolean(IStirFryRecipe.STIR_FRY_CAN));
            stirFry.setPendingItem(ItemStack.of(tag.getCompound(IStirFryRecipe.STIR_FRY_PENDING)));
            if (!tag.getString(IStirFryRecipe.STIR_FRY_RECIPE).isEmpty() && level != null) {
                Optional<?> rop = level.getRecipeManager().byKey(new ResourceLocation(tag.getString(IStirFryRecipe.STIR_FRY_RECIPE)));
                rop.ifPresent(r -> stirFry.setStirFryRecipe((StirFryRecipe) r));
            }
        }
        if (be instanceof ITeaProductionRecipe tp) {
            tp.setTeaPrdTimes(tag.getIntArray(ITeaProductionRecipe.TEA_PRODUCTION_TIME));
            tp.setTeaPrdRecipeTimes(tag.getIntArray(ITeaProductionRecipe.TEA_PRODUCTION_RECIPE_TIME));
        }
        if (be instanceof IGrillRecipe grill) {
            grill.setGrillTimes(tag.getIntArray(IGrillRecipe.GRILL_TIME));
            grill.setGrillRecipeTimes(tag.getIntArray(IGrillRecipe.GRILL_RECIPE_TIME));
        }
        if (be instanceof IFoodBoilingRecipe fb) {
            fb.setFBTimes(tag.getIntArray(IFoodBoilingRecipe.FB_TIME));
            fb.setFBRecipeTimes(tag.getIntArray(IFoodBoilingRecipe.FB_RECIPE_TIME));
        }
        if (be instanceof ISteamingRecipe steamer) {
            steamer.setSteamTimes(tag.getIntArray(ISteamingRecipe.STEAMER_TIME));
            steamer.setSteamRecipeTimes(tag.getIntArray(ISteamingRecipe.STEAMER_RECIPE_TIME));
            steamer.getInvList().deserializeNBT(tag.getList(ISteamingRecipe.STEAMER_INV_LIST, ListTag.TAG_COMPOUND));
        }
        if (be instanceof IBakingRecipe bake) {
            bake.setBakeTime(tag.getInt(IBakingRecipe.BAKE_TIME));
            bake.setBakeRecipeTime(tag.getInt(IBakingRecipe.BAKE_RECIPE_TIME));
        }
        if (be instanceof IFermentationRecipe f) {
            f.setFermentTime(tag.getInt(IFermentationRecipe.FERMENT_TIME));
            f.setFermentRecipeTime(tag.getInt(IFermentationRecipe.FERMENT_RECIPE_TIME));
        }
    }

    @SubscribeEvent
    public void cap(BlockEntityDataEvent.Capability event) {
        BlockEntity be = event.getBlockEntity();
        if (be instanceof ISteamingRecipe steamer) {
            if (event.getCap() == ForgeCapabilities.ITEM_HANDLER) {
                event.setReturnValue(LazyOptional.of(steamer::getInvList).cast());
            }
        }
    }

}
