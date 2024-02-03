package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PackToClient implements INetWorkReg {

    public static SimpleChannel INSTANCE;

    private int packetID = 0;
    private final SimpleChannel network;

    public PackToClient(ResourceLocation name) {
        this.network = NetworkRegistry.ChannelBuilder.named(name)
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

    private int id() {
        return packetID++;
    }

    @Override
    public void register() {
        INSTANCE = network;
    }

}
