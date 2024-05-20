package cn.solarmoon.idyllic_food_diary.core.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IBrewTeaRecipe;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IPendingResult;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISpiceable;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.ability.BlockEntityDataHolder;
import com.google.gson.JsonParser;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IMTileDataHolders {
    public static void register() {}

    public static final BlockEntityDataHolder<?> IKettleRecipeDataHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(IKettleRecipe.class)
            .save((kettle, nbt) -> nbt.putInt(NBTList.BOIL_TICK, kettle.getBoilTime()))
            .load((kettle, nbt) -> kettle.setBoilTime(nbt.getInt(NBTList.BOIL_TICK)))
            .build();

    public static final BlockEntityDataHolder<?> IBrewTeaRecipeDataHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(IBrewTeaRecipe.class)
            .save((brew, nbt) -> nbt.putInt(NBTList.BOIL_TICK, brew.getBrewTime()))
            .load((brew, nbt) -> brew.setBrewTime(nbt.getInt(NBTList.BOIL_TICK)))
            .build();

    public static final BlockEntityDataHolder<?> ISpiceableDataHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(ISpiceable.class)
            .save((spiceable, nbt) -> {
                ListTag listTag = new ListTag();
                for (var spice : spiceable.getSpices()) {
                    listTag.add(spice.serializeNBT());
                }
                nbt.put(NBTList.SPICES, listTag);
            })
            .load((spiceable, nbt) -> {
                ListTag listTag = nbt.getList(NBTList.SPICES, ListTag.TAG_COMPOUND);
                for (int i = 0; i < listTag.size(); i++) {
                    spiceable.getSpices().add(Spice.readFromNBT(listTag.getCompound(i)));
                }
            })
            .build();

    public static final BlockEntityDataHolder<?> IPendingResultHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(IPendingResult.class)
            .save((serveable, nbt) -> {
                nbt.put(NBTList.RESULT, serveable.getResult().serializeNBT());
                nbt.putString(NBTList.CONTAINER, serveable.getContainer().toJson().toString());
            })
            .load((serveable, nbt) -> {
                serveable.setResult(ItemStack.of(nbt.getCompound(NBTList.RESULT)));
                serveable.setContainer(Ingredient.fromJson(JsonParser.parseString(nbt.getString(NBTList.CONTAINER))));
            })
            .build();

}
