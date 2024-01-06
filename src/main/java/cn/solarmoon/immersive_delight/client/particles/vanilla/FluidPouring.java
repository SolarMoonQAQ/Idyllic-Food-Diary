package cn.solarmoon.immersive_delight.client.particles.vanilla;

import cn.solarmoon.immersive_delight.util.VecAlgorithm;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class FluidPouring {

    /**
     * 倒水粒子，根据倒的量增加粒子数
     */
    public static void fluidPouring(BlockState fluidState, int fluidAmount) {
        ParticleOptions particle = new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if(player == null) return;
        Vec3 spawnPos = VecAlgorithm.getSpawnPosFrontPlayer(player, 0.5);
        Vec3 spawnPos2 = VecAlgorithm.getSpawnPosFrontPlayer(player, 1.5);
        Vec3 spawnPos3 = VecAlgorithm.getSpawnPosFrontPlayer(player, 3);
        if (level != null) {
            for (int i = 0; i < fluidAmount / 30; i++) {
                Vec3 lookVec = player.getLookAngle();
                //偏移系数
                double delta = 30;
                Random random = new Random();
                double velocityX = lookVec.x * delta * random.nextFloat() + (random.nextFloat() - 0.5D) * delta;
                double velocityY = random.nextFloat() * 0.2D + (random.nextFloat() - 0.5D) * 10D;
                double velocityZ = lookVec.z * delta * random.nextFloat() + (random.nextFloat() - 0.5D) * delta;
                level.addParticle(particle, spawnPos.x, spawnPos.y, spawnPos.z, velocityX, velocityY, velocityZ);
                level.addParticle(particle, spawnPos2.x, spawnPos2.y, spawnPos2.z, velocityX, velocityY, velocityZ);
                level.addParticle(particle, spawnPos3.x, spawnPos3.y, spawnPos3.z, velocityX, velocityY, velocityZ);
            }
        }
    }



}
