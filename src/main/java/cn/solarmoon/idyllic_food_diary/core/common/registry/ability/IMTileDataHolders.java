package cn.solarmoon.idyllic_food_diary.core.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.common.ability.BlockEntityDataHolder;

public class IMTileDataHolders {
    public static void register() {}

    public static final BlockEntityDataHolder<?> IKettleRecipeDataHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(IKettleRecipe.class)
            .save((kettle, nbt) -> nbt.putInt(NBTList.BOIL_TICK, kettle.getBoilTime()))
            .load((kettle, nbt) -> kettle.setBoilTime(nbt.getInt(NBTList.BOIL_TICK)))
            .build();

}
