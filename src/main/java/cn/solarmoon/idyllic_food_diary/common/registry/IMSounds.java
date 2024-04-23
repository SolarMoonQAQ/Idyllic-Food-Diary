package cn.solarmoon.idyllic_food_diary.common.registry;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;
import cn.solarmoon.solarmoon_core.api.registry.object.SoundEntry;

public enum IMSounds implements IRegister {
    INSTANCE;

    public static final SoundEntry PLAYER_POUR = IdyllicFoodDiary.REGISTRY.sound()
            .name("player.pouring_water")
            .build();

    public static final SoundEntry PLAYER_SPILLING_WATER = IdyllicFoodDiary.REGISTRY.sound()
            .name("player.water_splashing")
            .build();

    public static final SoundEntry BOILING_WATER = IdyllicFoodDiary.REGISTRY.sound()
            .name("block.boiling_water")
            .build();

}
