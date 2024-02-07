package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.network.NetworkPack;
import cn.solarmoon.immersive_delight.api.registry.core.BasePackRegistry;
import cn.solarmoon.immersive_delight.network.handler.ClientPackHandler;
import cn.solarmoon.immersive_delight.network.handler.ServerPackHandler;
import net.minecraft.resources.ResourceLocation;


public class IMPacks extends BasePackRegistry {

    public final static NetworkPack CLIENT_PACK = new NetworkPack(new ResourceLocation(ImmersiveDelight.MOD_ID, "client"), NetworkPack.Side.CLIENT)
            .addHandler(new ClientPackHandler());
    public final static NetworkPack SERVER_PACK = new NetworkPack(new ResourceLocation(ImmersiveDelight.MOD_ID, "server"), NetworkPack.Side.SERVER)
            .addHandler(new ServerPackHandler());

    @Override
    public void addRegistry() {
        add(CLIENT_PACK);
        add(SERVER_PACK);
    }

}
