package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockRenderer;
import cn.solarmoon.solarmoon_core.api.entry.client.LayerEntry;

public class IMLayers {
    public static void register() {}

    public static final LayerEntry MILLSTONE = IdyllicFoodDiary.REGISTRY.layer()
            .id("millstone")
            .bound(MillstoneBlockRenderer::createBodyLayer)
            .build();

}
