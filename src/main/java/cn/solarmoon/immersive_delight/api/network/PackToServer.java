package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackToServer implements INetWorkReg {

    public static SimpleChannel INSTANCE;
    public static List<IServerPackHandler> serverPackHandlers = new ArrayList<>();

    private int packetID = 0;
    private final SimpleChannel network;

    public PackToServer(ResourceLocation name) {
        network = NetworkRegistry.ChannelBuilder.named(name)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();
        network.messageBuilder(ServerPackSerializer.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerPackSerializer::decode)
                .encoder(ServerPackSerializer::encode)
                .consumerMainThread(ServerPackSerializer::handle)
                .add();
    }

    private int id() {
        return packetID++;
    }

    public PackToServer addHandler(IServerPackHandler... handlers) {
        Collections.addAll(serverPackHandlers, handlers);
        return this;
    }

    @Override
    public void register() {
        INSTANCE = network;
    }

}
