package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.element.matter.durian.DurianEntityRenderer;
import cn.solarmoon.idyllic_food_diary.registry.common.IMEntityTypes;

public class IMEntityRenderers {

    public static void register() {
        IMEntityTypes.DURIAN_ENTITY.renderer(DurianEntityRenderer::new);
    }

}
