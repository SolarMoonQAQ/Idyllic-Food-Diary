package cn.solarmoon.immersive_delight.client.particles.vanilla;

import cn.solarmoon.immersive_delight.util.VecAlgorithm;
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
        Vec3 spawnPos = VecAlgorithm.getSpawnPosFrontPlayer(player, 1);
        level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
    }
}
