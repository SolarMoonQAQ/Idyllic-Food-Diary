package cn.solarmoon.idyllic_food_diary.core.network;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IBrewTeaRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.GrillBlockEntity;
import cn.solarmoon.idyllic_food_diary.api.common.block_entity.IKettleRecipe;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NBTList;
import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.solarmoon_core.api.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, String message) {
        switch (message) {
            case NETList.SYNC_UP_STEP -> {
                player.setMaxUpStep(f);
            }
            case NETList.SYNC_BURN_TIME -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.setBurnTime((int) f);
                }
            }
            case NETList.SYNC_BURN_TIME_SAVING -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof GrillBlockEntity grill) {
                    grill.saveBurnTime = (int) f;
                }
            }
            case NETList.SYNC_BOIL_TIME -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof IKettleRecipe kettle) {
                    kettle.setBoilTime(nbt.getInt(NBTList.BOIL_TICK));
                }
            }
            case NETList.SYNC_BREW_TIME -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof IBrewTeaRecipe brew) {
                    brew.setBrewTime(nbt.getInt(NBTList.BREW_TICK));
                }
            }
            case NETList.PARTICLE_EAT -> {
                Vec3 spawnPos = vec3List.get(0);
                Vec3 velocity = vec3List.get(1);
                ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
                double delta = (new Random().nextDouble() - 0.5) * 0.2;
                level.addParticle(particle, spawnPos.x, spawnPos.y-0.35, spawnPos.z + delta, velocity.x, velocity.y, velocity.z);
            }
            case NETList.PARTICLE_POUR -> {
                int fluidAmount = fluidStack.getAmount();
                BlockState fluidState = fluidStack.getFluid().defaultFluidState().createLegacyBlock();
                ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
                boolean flag = fluidStack.getTag() != null && fluidStack.getTag().contains("Potion");
                double d0=0, d1=0, d2=0;
                if (flag) {
                    particle = ParticleTypes.ENTITY_EFFECT;
                    ResourceLocation potionId = new ResourceLocation(TextUtil.extractTag(fluidStack.getTag().toString(), "Potion"));
                    Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
                    if (potion != null) {
                        int color = potion.getEffects().get(0).getEffect().getColor();
                        d0 = (double)(color >> 16 & 255) / 255.0D;
                        d1 = (double)(color >> 8 & 255) / 255.0D;
                        d2 = (double)(color & 255) / 255.0D;
                    }
                } else if (fluidStack.getFluid().getBucket().getDefaultInstance().is(Items.MILK_BUCKET)) {
                    particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_WOOL.defaultBlockState());
                }
                Vec3 spawnPos = vec3List.get(0);
                Vec3 spawnPos2 = vec3List.get(1);
                Vec3 spawnPos3 = vec3List.get(2);
                Vec3 lookVec = vec3List.get(3);
                for (int i = 0; i < fluidAmount / 30; i++) {
                    //偏移系数
                    double delta = 30;
                    Random random = new Random();
                    double velocityX = lookVec.x * delta * random.nextFloat() + (random.nextFloat() - 0.5D) * delta;
                    double velocityY = random.nextFloat() * 0.2D + (random.nextFloat() - 0.5D) * 10D;
                    double velocityZ = lookVec.z * delta * random.nextFloat() + (random.nextFloat() - 0.5D) * delta;
                    if (flag) {
                        level.addParticle(particle, spawnPos.x, spawnPos.y-0.3, spawnPos.z, d0, d1, d2);
                        level.addParticle(particle, spawnPos2.x, spawnPos2.y-0.3, spawnPos2.z, d0, d1, d2);
                        level.addParticle(particle, spawnPos3.x, spawnPos3.y-0.3, spawnPos3.z, d0, d1, d2);
                    } else {
                        level.addParticle(particle, spawnPos.x, spawnPos.y, spawnPos.z, velocityX, velocityY, velocityZ);
                        level.addParticle(particle, spawnPos2.x, spawnPos2.y, spawnPos2.z, velocityX, velocityY, velocityZ);
                        level.addParticle(particle, spawnPos3.x, spawnPos3.y, spawnPos3.z, velocityX, velocityY, velocityZ);
                    }
                }
            }
        }
    }

}
