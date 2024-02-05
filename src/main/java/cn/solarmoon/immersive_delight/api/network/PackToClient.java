package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.*;

public class PackToClient implements INetWorkReg {

    public static SimpleChannel INSTANCE;
    public static List<IClientPackHandler> clientPackHandlers = new ArrayList<>();

    private int packetID = 0;
    private final SimpleChannel network;

    public PackToClient(ResourceLocation name) {
        network = NetworkRegistry.ChannelBuilder.named(name)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();
        network.messageBuilder(ClientPackSerializer.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientPackSerializer::decode)
                .encoder(ClientPackSerializer::encode)
                .consumerMainThread(ClientPackSerializer::handle)
                .add();
    }

    public PackToClient addHandler(IClientPackHandler... handlers) {
        Collections.addAll(clientPackHandlers, handlers);
        return this;
    }

    private int id() {
        return packetID++;
    }

    @Override
    public void register() {
        INSTANCE = network;
    }

}
