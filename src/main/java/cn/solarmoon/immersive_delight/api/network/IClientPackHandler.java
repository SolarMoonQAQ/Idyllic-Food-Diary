package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;

public interface IClientPackHandler {

    void handle(ClientPackSerializer packet);

}
