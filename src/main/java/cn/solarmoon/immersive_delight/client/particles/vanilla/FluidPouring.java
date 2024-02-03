package cn.solarmoon.immersive_delight.client.particles.vanilla;

import cn.solarmoon.immersive_delight.util.CoreUtil;
import cn.solarmoon.immersive_delight.util.VecUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class FluidPouring {

    /**
     * 倒水粒子，根据倒的量增加粒子数
     */
    public static void fluidPouring(FluidStack fluidStack, int fluidAmount) {
        Minecraft mc = Minecraft.getInstance();
        BlockState fluidState = fluidStack.getFluid().defaultFluidState().createLegacyBlock();
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
        boolean flag = fluidStack.getTag() != null && fluidStack.getTag().contains("Potion");
        double d0=0,d1=0,d2=0;
        if (flag) {
            particle = ParticleTypes.ENTITY_EFFECT;
            ResourceLocation potionId = new ResourceLocation(cn.solarmoon.immersive_delight.api.util.TextUtil.extract(fluidStack.getTag().toString(), "Potion"));
            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion != null) {
                int color = potion.getEffects().get(0).getEffect().getColor();
                d0 = (double)(color >> 16 & 255) / 255.0D;
                d1 = (double)(color >> 8 & 255) / 255.0D;
                d2 = (double)(color & 255) / 255.0D;
                CoreUtil.deBug(potion.getEffects().get(0).getEffect().getDisplayName().getString());
            }
        } else if (fluidStack.getFluid().getBucket().getDefaultInstance().is(Items.MILK_BUCKET)) {
            particle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_WOOL.defaultBlockState());
        }
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if(player == null) return;
        Vec3 spawnPos = VecUtil.getSpawnPosFrontPlayer(player, 0.5);
        Vec3 spawnPos2 = VecUtil.getSpawnPosFrontPlayer(player, 1.5);
        Vec3 spawnPos3 = VecUtil.getSpawnPosFrontPlayer(player, 3);
        if (level != null) {
            for (int i = 0; i < fluidAmount / 30; i++) {
                Vec3 lookVec = player.getLookAngle();
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
