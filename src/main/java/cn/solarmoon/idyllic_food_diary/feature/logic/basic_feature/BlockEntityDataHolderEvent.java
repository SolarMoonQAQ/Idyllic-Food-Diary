package cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature;

import cn.solarmoon.idyllic_food_diary.feature.logic.evaporation.IEvaporationRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.soup.ISoupRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.logic.stew.IStewRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.stir_fry.IStirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.stir_fry.StirFryRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.feature.logic.water_boiling.IWaterBoilingRecipe;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.event.BlockEntityDataEvent;
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
            tag.putInt(NBTList.BOIL_TICK, kettle.getBoilTime());
        }
        if (be instanceof IBrewingRecipe brew) {
            tag.putInt(NBTList.BREW_TICK, brew.getBrewTime());
        }
        if (be instanceof ISpiceable spiceable) {
            ListTag listTag = spiceable.getSpices().serializeNBT();
            tag.put(NBTList.SPICES, listTag);
        }
        if (be instanceof IPendingResult pendingResult) {
            tag.put(NBTList.RESULT, pendingResult.getResult().serializeNBT());
            tag.putString(NBTList.CONTAINER, pendingResult.getContainer().toJson().toString());
        }
        if (be instanceof IExpGiver expGiver) {
            tag.putInt(NBTList.EXP, expGiver.getExp());
        }
        if (be instanceof IEvaporationRecipe eva) {
            tag.putInt(NBTList.DRAIN_TICK, eva.getEvaporationTick());
        }
        if (be instanceof IStewRecipe stew) {
            tag.putInt(NBTList.STEW_TIME, stew.getStewTime());
            tag.putInt(NBTList.STEW_RECIPE_TIME, stew.getStewRecipeTime());
        }
        if (be instanceof ISoupRecipe simmer) {
            tag.putInt(NBTList.SIMMER_TIME, simmer.getSimmerTime());
            tag.putInt(NBTList.STEW_RECIPE_TIME, simmer.getSimmerRecipeTime());
        }
        if (be instanceof ISimpleFuelBlockEntity fuel) {
            tag.putInt(NBTList.BURNING_TIME, fuel.getBurnTime());
            tag.putInt(NBTList.BURNING_TIME_SAVING, fuel.getFuelTime());
        }
        if (be instanceof IStirFryRecipe stirFry) {
            tag.putInt(NBTList.STIR_FRY_TIME, stirFry.getStirFryTime());
            tag.putInt(NBTList.STIR_FRY_RECIPE_TIME, stirFry.getStirFryRecipeTime());
            tag.putInt(NBTList.STIR_FRY_STAGE_NUM, stirFry.getPresentStage());
            tag.putInt(NBTList.STIR_FRY_COUNT, stirFry.getFryCount());
            tag.putBoolean(NBTList.STIR_FRY_CAN, stirFry.canStirFry());
            if (stirFry.getStirFryRecipe() != null) tag.putString(NBTList.STIR_FRY_RECIPE, stirFry.getStirFryRecipe().id().toString());
            tag.put(NBTList.STIR_FRY_PENDING, stirFry.getPendingItem().save(new CompoundTag()));
        }
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        Level level = be.getLevel();
        if (be instanceof IWaterBoilingRecipe kettle) {
            kettle.setBoilTime(tag.getInt(NBTList.BOIL_TICK));
        }
        if (be instanceof IBrewingRecipe brew) {
            brew.setBrewTime(tag.getInt(NBTList.BREW_TICK));
        }
        if (be instanceof ISpiceable spiceable) {
            spiceable.getSpices().deserializeNBT(tag.getList(NBTList.SPICES, ListTag.TAG_COMPOUND));
        }
        if (be instanceof IPendingResult pendingResult) {
            pendingResult.setResult(ItemStack.of(tag.getCompound(NBTList.RESULT)));
            pendingResult.setContainer(Ingredient.fromJson(JsonParser.parseString(tag.getString(NBTList.CONTAINER))));
        }
        if (be instanceof IExpGiver expGiver) {
            expGiver.setExp(tag.getInt(NBTList.EXP));
        }
        if (be instanceof IEvaporationRecipe eva) {
            eva.setEvaporationTick(tag.getInt(NBTList.DRAIN_TICK));
        }
        if (be instanceof IStewRecipe stew) {
            stew.setStewTime(tag.getInt(NBTList.STEW_TIME));
            stew.setStewRecipeTime(tag.getInt(NBTList.STEW_RECIPE_TIME));
        }
        if (be instanceof ISoupRecipe simmer) {
            simmer.setSimmerTime(tag.getInt(NBTList.SIMMER_TIME));
            simmer.setSimmerRecipeTime(tag.getInt(NBTList.SIMMER_RECIPE_TIME));
        }
        if (be instanceof ISimpleFuelBlockEntity fuel) {
            fuel.setBurnTime(tag.getInt(NBTList.BURNING_TIME));
            fuel.setFuelTime(tag.getInt(NBTList.BURNING_TIME_SAVING));
        }
        if (be instanceof IStirFryRecipe stirFry) {
            stirFry.setStirFryTime(tag.getInt(NBTList.STIR_FRY_TIME));
            stirFry.setStirFryRecipeTime(tag.getInt(NBTList.STIR_FRY_RECIPE_TIME));
            stirFry.setFryCount(tag.getInt(NBTList.STIR_FRY_COUNT));
            stirFry.setPresentStage(tag.getInt(NBTList.STIR_FRY_STAGE_NUM));
            stirFry.setCanStirFry(tag.getBoolean(NBTList.STIR_FRY_CAN));
            stirFry.setPendingItem(ItemStack.of(tag.getCompound(NBTList.STIR_FRY_PENDING)));
            if (!tag.getString(NBTList.STIR_FRY_RECIPE).isEmpty() && level != null) {
                Optional<?> rop = level.getRecipeManager().byKey(new ResourceLocation(tag.getString(NBTList.STIR_FRY_RECIPE)));
                rop.ifPresent(r -> stirFry.setStirFryRecipe((StirFryRecipe) r));
            }
        }
    }

}
