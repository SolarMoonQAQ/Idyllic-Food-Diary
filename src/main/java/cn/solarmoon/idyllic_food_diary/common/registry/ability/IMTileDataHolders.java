package cn.solarmoon.idyllic_food_diary.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.common.block_entity.iutor.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.api.ability.BlockEntityDataHolder;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;

public enum IMTileDataHolders implements IRegister {
    INSTANCE;

    public static final BlockEntityDataHolder<?> IKettleRecipeDataHolder = IdyllicFoodDiary.REGISTRY
            .blockEntityDataHolder(IKettleRecipe.class)
            .save((kettle, nbt) -> nbt.putInt(NBTList.BOIL_TICK, kettle.getBoilTime()))
            .load((kettle, nbt) -> kettle.setBoilTime(nbt.getInt(NBTList.BOIL_TICK)))
            .build();

}
