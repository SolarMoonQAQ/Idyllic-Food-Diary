package cn.solarmoon.immersive_delight.common.registry;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.registry.core.BaseObjectRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

public class IMSounds extends BaseObjectRegistry<SoundEvent> {

    //音效
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);

    public IMSounds() {
        super(SOUNDS);
    }

    public static final RegistryObject<SoundEvent> PLAYER_POUR = SOUNDS.register("player.pouring_water",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ImmersiveDelight.MOD_ID, "player.pouring_water")));

    public static final RegistryObject<SoundEvent> PLAYER_SPILLING_WATER = SOUNDS.register("player.water_splashing",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ImmersiveDelight.MOD_ID, "player.water_splashing")));

    public static final RegistryObject<SoundEvent> BOILING_WATER = SOUNDS.register("block.boiling_water",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ImmersiveDelight.MOD_ID, "block.boiling_water")));

}
