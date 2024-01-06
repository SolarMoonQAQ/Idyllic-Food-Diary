package cn.solarmoon.immersive_delight.client.particles.vanilla;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

import static cn.solarmoon.immersive_delight.util.Constants.mc;

public class Rolling {
    public static void rolling(BlockPos pos) {
        if(mc.level == null) return;
        ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, mc.level.getBlockState(pos).getBlock().asItem().getDefaultInstance());
        BlockState blockState = mc.level.getBlockState(pos);

        double minX = blockState.getShape(mc.level, pos).min(Direction.Axis.X);
        double minY = blockState.getShape(mc.level, pos).min(Direction.Axis.Y);
        double minZ = blockState.getShape(mc.level, pos).min(Direction.Axis.Z);
        double maxX = blockState.getShape(mc.level, pos).max(Direction.Axis.X);
        double maxY = blockState.getShape(mc.level, pos).max(Direction.Axis.Y);
        double maxZ = blockState.getShape(mc.level, pos).max(Direction.Axis.Z);

        Random random = new Random();
        double posX = pos.getX() + minX + (maxX - minX) * random.nextDouble();
        double posY = pos.getY() + minY + (maxY - minY) * random.nextDouble();
        double posZ = pos.getZ() + minZ + (maxZ - minZ) * random.nextDouble();

        double velocityX = (posX - (pos.getX() + 0.5)) * 0.2;
        double velocityY = (posY - (pos.getY() + maxY)) * 0.1;
        double velocityZ = (posZ - (pos.getZ() + 0.5)) * 0.2;

        for(int i = 0; i <5; i++) {
            mc.level.addParticle(particle, posX, posY, posZ, velocityX, velocityY, velocityZ);
        }

    }
}
