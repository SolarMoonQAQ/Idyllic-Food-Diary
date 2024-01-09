package cn.solarmoon.immersive_delight.client;


import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.init.ObjectRegister.SOUNDS;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMSounds {

    public static final RegistryObject<SoundEvent> PLAYER_POUR = SOUNDS.register("player.pouring_water",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ImmersiveDelight.MOD_ID, "player.pouring_water")));

    public static final RegistryObject<SoundEvent> PLAYER_SPILLING_WATER = SOUNDS.register("player.water_splashing",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ImmersiveDelight.MOD_ID, "player.water_splashing")));

}
