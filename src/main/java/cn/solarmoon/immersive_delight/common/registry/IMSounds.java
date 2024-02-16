package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.SoundEntry;

public enum IMSounds implements IRegister {
    INSTANCE;

    public static final SoundEntry PLAYER_POUR = ImmersiveDelight.REGISTRY.sound()
            .name("player.pouring_water")
            .build();

    public static final SoundEntry PLAYER_SPILLING_WATER = ImmersiveDelight.REGISTRY.sound()
            .name("player.water_splashing")
            .build();

    public static final SoundEntry BOILING_WATER = ImmersiveDelight.REGISTRY.sound()
            .name("block.boiling_water")
            .build();

}
