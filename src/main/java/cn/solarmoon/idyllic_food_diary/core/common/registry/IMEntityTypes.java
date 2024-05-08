package cn.solarmoon.idyllic_food_diary.core.common.registry;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.entity.DurianEntity;
import cn.solarmoon.solarmoon_core.api.common.registry.EntityEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class IMEntityTypes {
    public static void register() {}

    public static final EntityEntry<DurianEntity> DURIAN_ENTITY = IdyllicFoodDiary.REGISTRY.entity()
            .id("durian")
            .builder(EntityType.Builder
                    .<DurianEntity>of(DurianEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(4)
                    .updateInterval(20))
            .build();

}
