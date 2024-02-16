package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.entity_renderer.DurianEntityRenderer;
import cn.solarmoon.immersive_delight.common.entity.DurianEntity;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.EntityEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public enum IMEntityTypes implements IRegister {
    INSTANCE;

    public static final EntityEntry<DurianEntity> DURIAN_ENTITY = ImmersiveDelight.REGISTRY.entity()
            .id("durian")
            .builder(EntityType.Builder
                    .<DurianEntity>of(DurianEntity::new, MobCategory.MISC)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(4)
                    .updateInterval(20))
            .renderer(DurianEntityRenderer::new)
            .build();

}
