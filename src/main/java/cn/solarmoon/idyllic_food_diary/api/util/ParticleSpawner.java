package cn.solarmoon.idyllic_food_diary.api.util;

import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

public class ParticleSpawner {

    /**
     * 吃对应的方块粒子
     * @param pos 要吃的方块坐标
     * @param player 玩家
     */
    public static void eat(BlockPos pos, Player player, Level level) {
        double distanceInFront = 0.35D;
        Vec3 lookVec = player.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        Vec3 spawnPos = player.position().add(0, player.getEyeHeight(), 0).add(inFrontVec);
        ItemStack foodItem = new ItemStack(level.getBlockState(pos).getBlock().asItem());
        for(int i = 0; i < 5 ; i++) {
            Random rand = new Random();
            double scale = 0.1;
            double randomFactor = 0.2;
            Vec3 velocity = lookVec.scale(scale);
            velocity = velocity.add((rand.nextDouble() - 0.5) * randomFactor, (rand.nextDouble()) * 0.25, (rand.nextDouble() - 0.5) * randomFactor);
            if (!level.isClientSide) {
                IMPacks.CLIENT_PACK.getSender().send(NETList.PARTICLE_EAT, List.of(spawnPos, velocity), foodItem);
            }
        }
    }

    /**
     * 吃物品粒子
     * @param foodItem 要吃的物品
     * @param player 玩家
     */
    public static void eat(ItemStack foodItem, Player player, Level level) {
        double distanceInFront = 0.35D;
        Vec3 lookVec = player.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        Vec3 spawnPos = player.position().add(0, player.getEyeHeight(), 0).add(inFrontVec);
        for(int i = 0; i < 5 ; i++) {
            Random rand = new Random();
            double scale = 0.1;
            double randomFactor = 0.2;
            Vec3 velocity = lookVec.scale(scale);
            velocity = velocity.add((rand.nextDouble() - 0.5) * randomFactor, (rand.nextDouble()) * 0.25, (rand.nextDouble() - 0.5) * randomFactor);
            if (!level.isClientSide) {
                IMPacks.CLIENT_PACK.getSender().send(NETList.PARTICLE_EAT, List.of(spawnPos, velocity), foodItem);
            }
        }
    }

    /**
     * 倒水粒子，根据倒的量增加粒子数
     */
    public static void fluidPouring(FluidStack fluidStack, Player player, Level level) {
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(player, 0.5);
        Vec3 spawnPos2 = VecUtil.getSpawnPosFrontEntity(player, 1.5);
        Vec3 spawnPos3 = VecUtil.getSpawnPosFrontEntity(player, 3);
        Vec3 lookVec = player.getLookAngle();
        List<Vec3> vec3List = List.of(spawnPos, spawnPos2, spawnPos3, lookVec);
        if (!level.isClientSide) {
            IMPacks.CLIENT_PACK.getSender().send(NETList.PARTICLE_POUR, fluidStack, vec3List);
        }
    }

    public static void rolling(BlockPos pos, Level level) {
        ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, level.getBlockState(pos).getBlock().asItem().getDefaultInstance());
        BlockState blockState = level.getBlockState(pos);

        double minX = blockState.getShape(level, pos).min(Direction.Axis.X);
        double minY = blockState.getShape(level, pos).min(Direction.Axis.Y);
        double minZ = blockState.getShape(level, pos).min(Direction.Axis.Z);
        double maxX = blockState.getShape(level, pos).max(Direction.Axis.X);
        double maxY = blockState.getShape(level, pos).max(Direction.Axis.Y);
        double maxZ = blockState.getShape(level, pos).max(Direction.Axis.Z);

        Random random = new Random();
        double posX = pos.getX() + minX + (maxX - minX) * random.nextDouble();
        double posY = pos.getY() + minY + (maxY - minY) * random.nextDouble();
        double posZ = pos.getZ() + minZ + (maxZ - minZ) * random.nextDouble();

        double velocityX = (posX - (pos.getX() + 0.5)) * 0.2;
        double velocityY = (posY - (pos.getY() + maxY)) * 0.1;
        double velocityZ = (posZ - (pos.getZ() + 0.5)) * 0.2;

        for(int i = 0; i <5; i++) {
            level.addParticle(particle, posX, posY, posZ, velocityX, velocityY, velocityZ);
        }

    }

    // 显然这种方式太过取巧，还是需要一个更规范的tick方式
    @Deprecated
    @Mod.EventBusSubscriber(modid = IdyllicFoodDiary.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Wave {
        public static class DistanceHolder {
            double distance;
            int count;
        }


        public static void wave(ParticleOptions particle, BlockPos pos, Level level, float speed, int initialCount, double maxDistance, double minDistance){
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
                                level.addParticle(particle, x, y, z, dx, dy, dz);
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
}
