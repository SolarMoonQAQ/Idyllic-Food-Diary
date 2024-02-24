package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.effect.FleetOfFootEffect;
import cn.solarmoon.immersive_delight.common.effect.RefreshmentEffect;
import cn.solarmoon.immersive_delight.common.effect.SnugEffect;
import cn.solarmoon.immersive_delight.common.effect.TeaAromaEffect;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.EffectEntry;

public enum IMEffects implements IRegister {
    INSTANCE;

    public static final EffectEntry TEA_AROMA = ImmersiveDelight.REGISTRY.effect()
            .id("tea_aroma")
            .bound(TeaAromaEffect::new)
            .build();

    public static final EffectEntry REFRESHMENT = ImmersiveDelight.REGISTRY.effect()
            .id("refreshment")
            .bound(RefreshmentEffect::new)
            .build();

    public static final EffectEntry FLEET_OF_FOOT = ImmersiveDelight.REGISTRY.effect()
            .id("fleet_of_foot")
            .bound(FleetOfFootEffect::new)
            .build();

    public static final EffectEntry SNUG = ImmersiveDelight.REGISTRY.effect()
            .id("snug")
            .bound(SnugEffect::new)
            .build();

}
