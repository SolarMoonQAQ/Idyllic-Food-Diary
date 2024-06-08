package cn.solarmoon.idyllic_food_diary.feature.logic.spit_item;

import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpitUtil {

    /**
     * 吐子儿
     */
    public static void spit(Item item, int count, Player player) {
        Level level = player.level();
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(player, 0.5, -0.5);
        ItemEntity itemEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, new ItemStack(item, count));
        itemEntity.addDeltaMovement(player.getLookAngle().scale(0.3));
        itemEntity.setPickUpDelay(20);
        level.addFreshEntity(itemEntity);
    }

    /**
     * 吐子儿
     */
    public static void spit(ItemStack stack, Player player) {
        Level level = player.level();
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(player, 0.5, -0.5);
        ItemEntity itemEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, stack);
        itemEntity.addDeltaMovement(player.getLookAngle().scale(0.3));
        itemEntity.setPickUpDelay(20);
        level.addFreshEntity(itemEntity);
    }

}
