package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.api.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseContainerBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.immersive_delight.api.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.immersive_delight.api.network.IClientPackHandler;
import cn.solarmoon.immersive_delight.api.network.serializer.ClientPackSerializer;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import cn.solarmoon.immersive_delight.api.util.CapabilityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.Wave.wave;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(ClientPackSerializer packet) {
        //快乐的定义时间
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        BlockPos pos = packet.pos();
        List<ItemStack> stacks = packet.stacks();
        FluidStack fluidStack = packet.fluidStack();
        CompoundTag tag = packet.tag();
        float f = packet.f();
        String string = packet.string();
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
            case "updateCBlock" -> {
                if (level == null || pos == null) return;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseContainerBlockEntity c) {
                    c.setInventory(tag);
                }
            }
            case "updateTBlock" -> {
                if (level == null || pos == null) return;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
                    tankBlockEntity.setFluid(fluidStack);
                }
            }
            case "updateTCBlock" -> {
                if (level == null || pos == null) return;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTCBlockEntity tc) {
                    tc.setFluid(fluidStack);
                    tc.setInventory(tag);
                }
            }
            case "updateUpStep" -> {
                if (player != null) {
                    player.setMaxUpStep(0.6f);
                }
            }
            case "syncIndex" -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setIndex((int) f, recipeType);
            }
            case "syncRIndex" -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setRecipeIndex((int) f, recipeType);
            }
        }
    }

}
