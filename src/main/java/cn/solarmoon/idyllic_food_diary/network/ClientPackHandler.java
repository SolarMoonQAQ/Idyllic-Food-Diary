package cn.solarmoon.idyllic_food_diary.network;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.AbstractKettleItem;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


public class ClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag nbt, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, int i, String message) {
        switch (message) {
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
                Potion potion = PotionUtils.getPotion(fluidStack.getTag());
                double d0=0, d1=0, d2=0;
                if (potion != Potions.EMPTY) {
                    particle = ParticleTypes.ENTITY_EFFECT;
                    int color = potion.getEffects().get(0).getEffect().getColor();
                    d0 = (double)(color >> 16 & 255) / 255.0D;
                    d1 = (double)(color >> 8 & 255) / 255.0D;
                    d2 = (double)(color & 255) / 255.0D;
                } else if (fluidStack.getFluid().getBucket().getDefaultInstance().is(Items.MILK_BUCKET)) {
                    particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_WOOL.defaultBlockState());
                }
                Vec3 spawnPos = vec3List.get(0);
                Vec3 spawnPos2 = vec3List.get(1);
                Vec3 spawnPos3 = vec3List.get(2);
                Vec3 lookVec = vec3List.get(3);
                for (int n = 0; n < fluidAmount / 30; n++) {
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
            case NETList.PARTICLE_TEMP_COOL -> {
                Vec3 c = pos.getCenter();
                IntStream.range(0, 2).forEach(n -> level.addParticle(ParticleTypes.ITEM_SNOWBALL, c.x, c.y, c.z, 0, 0.1, 0));
            }
            case NETList.ADD_SPICE_PARTICLE -> {
                Random random = new Random();
                for (int n = 0; n < 7; ++n) {
                    double d0 = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    level.addParticle(ParticleTypes.COMPOSTER,
                            pos.getX() + random.nextFloat(),
                            pos.getY() + random.nextDouble(),
                            pos.getZ() + random.nextFloat(),
                            d0, d1, d2);
                }
            }
        }
    }

}
