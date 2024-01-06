package cn.solarmoon.immersive_delight.network.serializer;

import cn.solarmoon.immersive_delight.network.handler.ServerPackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

import static cn.solarmoon.immersive_delight.network.IMPackServer.instanceServer;

public record ServerPackSerializer(@Nullable BlockPos pos, @Nullable Block block, @Nullable ItemStack stack, String message) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(Objects.requireNonNullElse(pos, BlockPos.ZERO));
        ResourceLocation blockRegistryName = ForgeRegistries.BLOCKS.getKey(Objects.requireNonNullElse(block, Blocks.AIR));
        buf.writeResourceLocation(blockRegistryName);
        buf.writeItemStack(Objects.requireNonNullElse(stack, ItemStack.EMPTY), true);
    }

    public static ServerPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();
        ResourceLocation blockRegistryName = buf.readResourceLocation();
        Block block = ForgeRegistries.BLOCKS.getValue(blockRegistryName);
        ItemStack stack = buf.readItem();
        return new ServerPackSerializer(pos, block, stack, message);
    }

    public static void sendPacket(BlockPos pos, Block block, ItemStack stack, String message) {
        instanceServer.sendToServer(new ServerPackSerializer(pos, block, stack, message));
    }

    public static void handle(ServerPackSerializer packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> ServerPackHandler.serverPackHandler(packet, context));
        supplier.get().setPacketHandled(true);
    }

}

