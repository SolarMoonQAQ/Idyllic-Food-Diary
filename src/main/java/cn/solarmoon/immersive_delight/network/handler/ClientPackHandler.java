package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseContainerTankBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entities.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Wave.wave;


public class ClientPackHandler {

    public static void clientPackHandler(ClientPackSerializer packet) {

        //快乐的定义时间
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        BlockPos pos = packet.pos();
        List<ItemStack> stacks = packet.stacks();
        FluidStack fluidStack = packet.fluidStack();
        CompoundTag tag = packet.tag();
        float f = packet.f();
        String string = packet.message();
        //处理
        switch (packet.message()) {
            case "wave" -> {
                if (level == null || pos == null) return;
                BlockState state = level.getBlockState(pos);
                ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
                wave(particle, pos, 5, 10, Math.max(f / 5, 2), 0.8);
            }
            case "updateFurnaceStack" -> {
                if (level == null || pos == null) return;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case "updateCTBlock" -> {
                if (level == null || pos == null) return;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
                    tankBlockEntity.setFluid(fluidStack);
                    if(tankBlockEntity instanceof BaseContainerTankBlockEntity ct) {
                        ct.inventory.deserializeNBT(tag.getCompound("inventory"));
                    }
                }
            }
            case "updateUpStep" -> {
                if (player != null) {
                    player.setMaxUpStep(0.6f);
                }
            }
        }
    }

}
