package cn.solarmoon.idyllic_food_diary.core.client.registry;

import cn.solarmoon.idyllic_food_diary.core.client.entity_renderer.DurianEntityRenderer;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMEntityTypes;

public class IMEntityRenderers {

    public static void register() {
        IMEntityTypes.DURIAN_ENTITY.renderer(DurianEntityRenderer::new);
    }

}
