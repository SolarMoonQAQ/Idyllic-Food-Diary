package cn.solarmoon.immersive_delight.api.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.common.capability.IPlayerData;
import cn.solarmoon.immersive_delight.api.common.capability.PlayerData;
import cn.solarmoon.immersive_delight.api.registry.core.BaseCapabilityRegistry;
import cn.solarmoon.immersive_delight.api.registry.core.BaseFMLEventRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Capabilities extends BaseCapabilityRegistry {

    public static final Capability<IPlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    @Override
    public void addRegistry() {
        add(IPlayerData.class);
    }

    @Override
    public void attachCapabilities() {
        attach(new ResourceLocation(ImmersiveDelight.MOD_ID, "player_data"), new PlayerData(player));
    }

}
