package cn.solarmoon.immersive_delight.client.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.client.humanoid_model.ConjurerHatModel;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.LayerEntry;

public enum IMLayers implements IRegister {
    INSTANCE;

    public static final LayerEntry BATHROBE = ImmersiveDelight.REGISTRY.layer()
            .id("bathrobe")
            .bound(ConjurerHatModel::createLayer)
            .build();

}
