package cn.solarmoon.immersive_delight.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class LevelSummonHelper {

    /**
     * 生成基础掉落物
     * 概率掉落
     */
    public static void summonDrop(Item item, Level level, BlockPos pos, int origin, int bound) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, rand.nextInt(origin,bound)));
        drop.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     * 概率掉落
     */
    public static void summonDrop(Item item, Level level, Vec3 vec3, int origin, int bound) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(item, rand.nextInt(origin,bound)));
        drop.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     */
    public static void summonDrop(Item item, Level level, BlockPos pos, int amount) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, amount));
        drop.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     */
    public static void summonDrop(Item item, Level level, Vec3 vec3, int amount) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(item, amount));
        drop.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     */
    public static void summonDrop(ItemStack stack, Level level, BlockPos pos) {
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        drop.setDeltaMovement(level.random.nextGaussian() * 0.05, level.random.nextGaussian() * 0.05 + 0.2, level.random.nextGaussian() * 0.05);
        level.addFreshEntity(drop);
    }

}
