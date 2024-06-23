package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry.StirFryRecipe;
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
        if (be instanceof IPendingResult pendingResult) {
            tag.put(IPendingResult.RESULT, pendingResult.getResult().serializeNBT());
            tag.putString(IPendingResult.CONTAINER, pendingResult.getContainer().toJson().toString());
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
        if (be instanceof IPendingResult pendingResult) {
            pendingResult.setResult(ItemStack.of(tag.getCompound(IPendingResult.RESULT)));
            if (tag.contains(IPendingResult.CONTAINER)) pendingResult.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(IPendingResult.CONTAINER))));
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
    }

}
