package cn.solarmoon.immersive_delight.client.particle.vanilla;

import cn.solarmoon.solarmoon_core.util.VecUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public class Sweep {
    public static void sweep() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        if(player == null || level == null) return;
        Vec3 spawnPos = VecUtil.getSpawnPosFrontPlayer(player, 1);
        level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
    }
}
