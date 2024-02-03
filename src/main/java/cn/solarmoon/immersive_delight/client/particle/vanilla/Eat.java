package cn.solarmoon.immersive_delight.client.particle.vanilla;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class Eat {
    public static void eat(BlockPos pos) {
        //吃的粒子效果
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if(player == null || level == null) return;
        double distanceInFront = 0.35D;
        Vec3 lookVec = player.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        Vec3 spawnPos = player.position().add(0, player.getEyeHeight(), 0).add(inFrontVec);
        ItemStack foodItem = new ItemStack(level.getBlockState(pos).getBlock().asItem());
        ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, foodItem);
        for(int i = 0; i < 5 ; i++) {
            Random rand = new Random();
            double scale = 0.1;
            double randomFactor = 0.2;
            double delta = (rand.nextDouble() - 0.5) * 0.2;
            Vec3 velocity = lookVec.scale(scale);
            velocity = velocity.add((rand.nextDouble() - 0.5) * randomFactor, (rand.nextDouble()) * 0.25, (rand.nextDouble() - 0.5) * randomFactor);
            level.addParticle(particle, spawnPos.x, spawnPos.y-0.35, spawnPos.z + delta, velocity.x, velocity.y, velocity.z);
        }
    }
}
