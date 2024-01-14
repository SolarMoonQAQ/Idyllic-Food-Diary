package cn.solarmoon.immersive_delight.api.network.serializer;

import cn.solarmoon.immersive_delight.api.network.Pack;
import cn.solarmoon.immersive_delight.network.handler.ClientPackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;


public record ClientPackSerializer(@Nullable BlockPos pos, List<ItemStack> stacks, FluidStack fluidStack, CompoundTag tag, float f, String message) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(Objects.requireNonNullElse(pos, BlockPos.ZERO));

        buf.writeInt(stacks.size());
        for (ItemStack stack : stacks) {
            buf.writeItem(stack);
        }

        buf.writeFluidStack(fluidStack);
        buf.writeNbt(tag);
        buf.writeFloat(f);
    }

    public static ClientPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();

        int size = buf.readInt();
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            stacks.add(buf.readItem());
        }

        FluidStack fluidStack = buf.readFluidStack();
        CompoundTag tag = buf.readNbt();
        float f = buf.readFloat();
        return new ClientPackSerializer(pos, stacks, fluidStack, tag, f ,message);
    }

    public static void sendPacket(BlockPos pos, List<ItemStack> stacks, FluidStack fluidStack, CompoundTag tag, float f, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, stacks, fluidStack, tag, f, message));
    }

    public static void sendPacket(BlockPos pos, List<ItemStack> stacks, FluidStack fluidStack, float f, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, stacks, fluidStack, new CompoundTag(), f, message));
    }

    public static void sendPacket(BlockPos pos, List<ItemStack> stacks, float f, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, stacks, FluidStack.EMPTY, new CompoundTag(), f, message));
    }

    public static void sendPacket(BlockPos pos, float f, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, new ArrayList<>(), FluidStack.EMPTY, new CompoundTag(), f, message));
    }

    public static void sendPacket(BlockPos pos, FluidStack fluidStack, CompoundTag tag, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, new ArrayList<>(), fluidStack, tag, 0, message));
    }

    public static void sendPacket(BlockPos pos, FluidStack fluidStack, String message) {
        Pack.ToClient.instanceClient.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(pos, new ArrayList<>(), fluidStack, new CompoundTag(), 0, message));
    }

    public static void handle(ClientPackSerializer packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPackHandler.clientPackHandler(packet)));
        supplier.get().setPacketHandled(true);
    }

}
