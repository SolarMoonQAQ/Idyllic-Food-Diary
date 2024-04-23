package cn.solarmoon.idyllic_food_diary.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.common.block_entity.iutor.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.common.registry.IMPacks;
import cn.solarmoon.idyllic_food_diary.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.ability.BasicEntityBlockTicker;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;

public enum IMTickers implements IRegister {
    INSTANCE;

    public static final BasicEntityBlockTicker<?> IKettleRecipeClientSync = IdyllicFoodDiary.REGISTRY
            .basicEntityBlockTicker(IKettleRecipe.class)
            .addSynchronizer((pair, nbt) -> {
                nbt.putInt(NBTList.BOIL_TICK, pair.getA().getBoilTime());
                IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_BOIL_TIME, pair.getB().getBlockPos(), nbt);
                if (pair.getA().isBoiling()) IdyllicFoodDiary.DEBUG.send(pair.getA().getBoilTime() + "");
            })
            .build();

}
