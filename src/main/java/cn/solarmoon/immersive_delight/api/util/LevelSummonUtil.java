package cn.solarmoon.immersive_delight.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class LevelSummonUtil {

    /**
     * 生成基础掉落物
     * 坐标位置
     * 固定概率掉落
     */
    public static void summonDrop(Item item, Level level, BlockPos pos, int origin, int bound) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, rand.nextInt(origin,bound)));
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     * 更为精确的位置
     * 固定概率掉落
     */
    public static void summonDrop(Item item, Level level, Vec3 vec3, int origin, int bound) {
        Random rand = new Random();
        ItemEntity drop = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(item, rand.nextInt(origin,bound)));
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     */
    public static void summonDrop(Item item, Level level, BlockPos pos, int amount) {
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, amount));
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     */
    public static void summonDrop(Item item, Level level, Vec3 vec3, int amount) {
        ItemEntity drop = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(item, amount));
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     * 以坐标为生成点
     */
    public static void summonDrop(ItemStack stack, Level level, BlockPos pos) {
        ItemEntity drop = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 生成基础掉落物
     * 以三维坐标为生成点（更为精细）
     */
    public static void summonDrop(ItemStack item, Level level, Vec3 vec3) {
        ItemEntity drop = new ItemEntity(level, vec3.x, vec3.y, vec3.z, item);
        setMovement(drop);
        level.addFreshEntity(drop);
    }

    /**
     * 像原版掉落物一样给予掉落物随机运动轨迹
     */
    public static void setMovement(Entity entity) {
        Level level = entity.level();
        RandomSource random = level.random;
        entity.setDeltaMovement(
                (0.5 - random.nextFloat()) * 0.2,
                0.2,
                (0.5 - random.nextFloat()) * 0.2);
    }

    /**
     * 像give一样给玩家添加物品，如果没能成功，则将物品以掉落物的形式给到玩家身边
     */
    public static void addItemToInventory(Player player, ItemStack stack) {
        boolean result = player.addItem(stack);
        if (!result) {
            player.drop(stack, false);
        }
    }

    /**
     * 像give一样给玩家添加物品，如果没能成功，则将物品以掉落物的形式给到指定坐标（坐标中心）
     */
    public static void addItemToInventory(Player player, ItemStack stack, BlockPos pos) {
        boolean result = player.addItem(stack);
        if (!result) {
            Vec3 vec3 = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            summonDrop(stack, player.level(), vec3);
        }
    }

    /**
     * 像give一样给玩家添加物品，如果没能成功，则将物品以掉落物的形式给到指定坐标（坐标中心）
     * 完美模式：当还剩1个物品时能够把返还的物品准确回到点击的手上（仅限于物品交互，空手交互不可用此方法）
     */
    public static void addItemToInventoryPerfectly(Player player, ItemStack add, BlockPos pos, ItemStack heldItem, InteractionHand hand) {
        if (heldItem.getCount() == 1) player.setItemInHand(hand, add);
        else addItemToInventory(player, add, pos);
    }

    /**
     * 像give一样给玩家添加物品，如果没能成功，则将物品以掉落物的形式给到玩家身边
     * 完美模式：当还剩1个物品时能够把返还的物品准确回到点击的手上（仅限于物品交互，空手交互不可用此方法）
     */
    public static void addItemToInventoryPerfectly(Player player, ItemStack add, ItemStack heldItem, InteractionHand hand) {
        if (heldItem.getCount() == 1) player.setItemInHand(hand, add);
        else addItemToInventory(player, add);
    }

}
