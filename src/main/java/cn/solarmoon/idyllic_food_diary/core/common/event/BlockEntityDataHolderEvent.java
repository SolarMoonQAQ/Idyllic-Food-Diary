package cn.solarmoon.idyllic_food_diary.core.common.event;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.*;
import cn.solarmoon.idyllic_food_diary.api.tea_brewing.IBrewingRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.event.BlockEntityDataEvent;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
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
    }

}
