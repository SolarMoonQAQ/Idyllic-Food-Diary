package cn.solarmoon.idyllic_food_diary.core.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.api.common.ability.BasicEntityBlockTicker;

public class IMTickers {
    public static void register() {}

    public static final BasicEntityBlockTicker<?> IKettleRecipeClientSync = IdyllicFoodDiary.REGISTRY
            .basicEntityBlockTicker(IKettleRecipe.class)
            .addSynchronizer((pair, nbt) -> {
                nbt.putInt(NBTList.BOIL_TICK, pair.getA().getBoilTime());
                IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_BOIL_TIME, pair.getB().getBlockPos(), nbt);
                if (pair.getA().isBoiling()) IdyllicFoodDiary.DEBUG.send(pair.getA().getBoilTime() + "");
            })
            .build();

}
