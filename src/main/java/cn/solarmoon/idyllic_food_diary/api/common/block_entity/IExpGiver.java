package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

/**
 * 经验给予器，接入后给方块实体存储将要给予的经验功能，手动调用give方法以在合适的地方给予玩家经验
 */
public interface IExpGiver {

    int getExp();

    void setExp(int exp);

    /**
     * 给予玩家储存的经验，给予完后经验会重置为0
     * @param reset 是否重置经验为0
     */
    default boolean giveExp(Player player, boolean reset) {
        if (getExp() != 0) {
            player.giveExperiencePoints(getExp());
            player.level().playSound(null, player.getOnPos(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS);
            if (reset) resetExp();
            return true;
        }
        return false;
    }

    default void resetExp() {
        setExp(0);
    }

}
