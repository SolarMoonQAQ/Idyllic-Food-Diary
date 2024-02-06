package cn.solarmoon.immersive_delight.api.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.network.NetworkPack;
import cn.solarmoon.immersive_delight.api.network.handler.BaseClientPackHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Packs extends BasePackRegistry {

    public static final NetworkPack BASE_CLIENT_PACK = new NetworkPack(new ResourceLocation(ImmersiveDelight.MOD_ID, "base_client"), NetworkPack.Side.CLIENT)
            .addHandler(new BaseClientPackHandler());

    @Override
    public void addRegistry() {
        packs.add(BASE_CLIENT_PACK);
    }

    private static final Map<ResourceLocation, NetworkPack> packMap = new HashMap<>();

    public static NetworkPack get(ResourceLocation name) {
        return packMap.get(name);
    }

    public static Collection<NetworkPack> get() {
        return packMap.values();
    }

    public static void put(ResourceLocation name, NetworkPack pack) {
        packMap.put(name, pack);
    }

}
