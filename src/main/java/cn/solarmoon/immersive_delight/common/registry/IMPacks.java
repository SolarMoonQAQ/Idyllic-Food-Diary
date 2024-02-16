package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.network.handler.ClientPackHandler;
import cn.solarmoon.immersive_delight.network.handler.ServerPackHandler;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.NetPackEntry;


public enum IMPacks implements IRegister {
    INSTANCE;

    public final static NetPackEntry CLIENT_PACK = ImmersiveDelight.REGISTRY.netPack()
            .id("client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new ClientPackHandler())
            .build();

    public final static NetPackEntry SERVER_PACK = ImmersiveDelight.REGISTRY.netPack()
            .id("server")
            .side(NetPackEntry.Side.SERVER)
            .addHandler(new ServerPackHandler())
            .build();

}
