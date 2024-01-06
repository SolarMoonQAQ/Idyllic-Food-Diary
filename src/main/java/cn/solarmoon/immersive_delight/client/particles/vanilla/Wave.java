package cn.solarmoon.immersive_delight.client.particles.vanilla;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static cn.solarmoon.immersive_delight.util.Constants.mc;



@Mod.EventBusSubscriber(modid = ImmersiveDelight.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Wave {
    public static class DistanceHolder {
        double distance;
        int count;
    }


    public static void wave(ParticleOptions particle, BlockPos pos, float speed, int initialCount, double maxDistance, double minDistance){
        DistanceHolder holder = new DistanceHolder();
        holder.distance = minDistance;
        holder.count = initialCount;
        MinecraftForge.EVENT_BUS.register(new Object() {
            int tickCount = 0;

            @SubscribeEvent
            public void onTick(TickEvent.ClientTickEvent event) {
                if (event.phase == TickEvent.Phase.END) {
                    if (holder.distance <= maxDistance) {
                        for (int i = 0; i < holder.count; i++) {
                            Random random = new Random();
                            double angle = 2 * Math.PI * random.nextDouble();
                            double x = pos.getX() + 0.5D + holder.distance * Math.cos(angle);
                            double y = pos.getY();
                            double z = pos.getZ() + 0.5D + holder.distance * Math.sin(angle);
                            double dx = random.nextFloat() * (random.nextBoolean() ? 1 : -1);
                            double dy = random.nextFloat() * (random.nextBoolean() ? 1 : -1);
                            double dz = random.nextFloat() * (random.nextBoolean() ? 1 : -1);
                            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
                            dx /= length;
                            dy /= length;
                            dz /= length;
                            if (mc.level != null) {
                                mc.level.addParticle(particle, x, y, z, dx, dy, dz);
                            }
                        }
                        holder.distance += speed * 0.1;
                        holder.count++;
                    }
                    tickCount++;
                    if (tickCount >= maxDistance * 10) {
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            }
        });
    }

}



