package cn.solarmoon.idyllic_food_diary.client.registry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.client.humanoid_model.ConjurerHatModel;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;
import cn.solarmoon.solarmoon_core.api.registry.object.LayerEntry;

public enum IMLayers implements IRegister {
    INSTANCE;

    public static final LayerEntry BATHROBE = IdyllicFoodDiary.REGISTRY.layer()
            .id("bathrobe")
            .bound(ConjurerHatModel::createLayer)
            .build();

}
