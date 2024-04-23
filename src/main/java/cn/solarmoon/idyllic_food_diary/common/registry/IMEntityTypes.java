package cn.solarmoon.idyllic_food_diary.common.registry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.client.entity_renderer.DurianEntityRenderer;
import cn.solarmoon.idyllic_food_diary.common.entity.DurianEntity;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;
import cn.solarmoon.solarmoon_core.api.registry.object.EntityEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public enum IMEntityTypes implements IRegister {
    INSTANCE;

    public static final EntityEntry<DurianEntity> DURIAN_ENTITY = IdyllicFoodDiary.REGISTRY.entity()
            .id("durian")
            .builder(EntityType.Builder
                    .<DurianEntity>of(DurianEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(4)
                    .updateInterval(20))
            .renderer(DurianEntityRenderer::new)
            .build();

}
