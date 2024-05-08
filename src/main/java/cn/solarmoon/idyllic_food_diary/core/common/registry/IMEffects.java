package cn.solarmoon.idyllic_food_diary.core.common.registry;


import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.effect.FleetOfFootEffect;
import cn.solarmoon.idyllic_food_diary.core.common.effect.RefreshmentEffect;
import cn.solarmoon.idyllic_food_diary.core.common.effect.SnugEffect;
import cn.solarmoon.idyllic_food_diary.core.common.effect.TeaAromaEffect;
import cn.solarmoon.solarmoon_core.api.common.registry.EffectEntry;

public class IMEffects {
    public static void register() {}

    public static final EffectEntry TEA_AROMA = IdyllicFoodDiary.REGISTRY.effect()
            .id("tea_aroma")
            .bound(TeaAromaEffect::new)
            .build();

    public static final EffectEntry REFRESHMENT = IdyllicFoodDiary.REGISTRY.effect()
            .id("refreshment")
            .bound(RefreshmentEffect::new)
            .build();

    public static final EffectEntry FLEET_OF_FOOT = IdyllicFoodDiary.REGISTRY.effect()
            .id("fleet_of_foot")
            .bound(FleetOfFootEffect::new)
            .build();

    public static final EffectEntry SNUG = IdyllicFoodDiary.REGISTRY.effect()
            .id("snug")
            .bound(SnugEffect::new)
            .build();

}
