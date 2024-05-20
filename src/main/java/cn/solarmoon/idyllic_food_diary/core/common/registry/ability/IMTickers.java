package cn.solarmoon.idyllic_food_diary.core.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IBrewTeaRecipe;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISpiceable;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.api.common.ability.BasicEntityBlockTicker;

import java.util.List;

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

    public static final BasicEntityBlockTicker<?> IBrewTeaRecipeClientSync = IdyllicFoodDiary.REGISTRY
            .basicEntityBlockTicker(IBrewTeaRecipe.class)
            .addSynchronizer((pair, nbt) -> {
                nbt.putInt(NBTList.BREW_TICK, pair.getA().getBrewTime());
                IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_BREW_TIME, pair.getB().getBlockPos(), nbt);
                if (pair.getA().isBrewing()) IdyllicFoodDiary.DEBUG.send(pair.getA().getBrewTime() + "");
            })
            .build();

    /**
     * 对可放调料的方块实体设定每tick按条件清空其中调料
     */
    public static final BasicEntityBlockTicker<?> ISpiceableResetSpices = IdyllicFoodDiary.REGISTRY
            .basicEntityBlockTicker(ISpiceable.class)
            .add((pair) -> {
                ISpiceable spiceable = pair.getA();
                if (spiceable.timeToResetSpices()) {
                    var listCopy = List.copyOf(spiceable.getSpices());
                    spiceable.clearSpices();
                    if (!listCopy.isEmpty()) { // 只在变化时changed 因为IAnimateTicker会被重置为0很尴尬
                        pair.getB().setChanged();
                    }
                }
            })
            .build();

}
