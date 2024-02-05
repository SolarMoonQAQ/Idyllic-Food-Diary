package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import net.minecraftforge.network.NetworkEvent;

public interface IServerPackHandler {

    void handle(ServerPackSerializer packet, NetworkEvent.Context context);

}
