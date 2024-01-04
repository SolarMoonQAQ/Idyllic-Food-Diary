package cn.solarmoon.immersive_delight.particles.methods;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class Sweep {
    public static void sweep() {
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if(player == null || level == null) return;
        double distanceInFront = 1D;
        Vec3 lookVec = player.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        Vec3 spawnPos = player.position().add(0, player.getEyeHeight(), 0).add(inFrontVec);
        level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
    }
}
