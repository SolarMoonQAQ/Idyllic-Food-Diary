package cn.solarmoon.immersive_delight.api.network;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Pack {

    public static class ToServer {
        public static SimpleChannel instanceServer;
        private int packetID = 0;

        private int id() {
            return packetID++;
        }

        public void register() {

            SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ImmersiveDelight.MOD_ID, "server"))
                    .networkProtocolVersion(() -> "1")
                    .clientAcceptedVersions(string -> true)
                    .serverAcceptedVersions(string -> true)
                    .simpleChannel();

            instanceServer = network;

            network.messageBuilder(ServerPackSerializer.class, id(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(ServerPackSerializer::decode)
                    .encoder(ServerPackSerializer::encode)
                    .consumerMainThread(ServerPackSerializer::handle)
                    .add();
        }
    }

    public static class ToClient {
        public static SimpleChannel instanceClient;
        private int packetID = 0;

        private int id() {
            return packetID++;
        }

        public void register() {

            SimpleChannel network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(ImmersiveDelight.MOD_ID, "client"))
                    .networkProtocolVersion(() -> "1")
                    .clientAcceptedVersions(string -> true)
                    .serverAcceptedVersions(string -> true)
                    .simpleChannel();

            instanceClient = network;

            network.messageBuilder(ClientPackSerializer.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(ClientPackSerializer::decode)
                    .encoder(ClientPackSerializer::encode)
                    .consumerMainThread(ClientPackSerializer::handle)
                    .add();
        }
    }

}
