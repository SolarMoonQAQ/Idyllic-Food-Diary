package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersUtil;
import cn.solarmoon.idyllic_food_diary.data.IMBlockTags;
import cn.solarmoon.idyllic_food_diary.registry.common.IMDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

/**
 * 杂项实用方法
 */
public class FarmerUtil {

    /**
     * 检查是否为热源
     */
    public static boolean isHeatSource(BlockState state) {
        boolean commonFlag = state.is(IMBlockTags.HEAT_SOURCE) || FarmersUtil.isHeatSource(state);
        if (state.hasProperty(LIT)) {
            return commonFlag && state.getValue(LIT);
        }
        return commonFlag;
    }

    public static boolean isOnHeatSource(BlockEntity be) {
        Level level = be.getLevel();
        if (level == null) return false;
        BlockPos pos = be.getBlockPos();
        BlockState below = level.getBlockState(pos.below());
        return isHeatSource(below);
    }

    public static void tasteBudShock(LivingEntity entity) {
        Level level = entity.level();
        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        bolt.setPos(entity.getPosition(0));
        bolt.setDamage(0);
        level.addFreshEntity(bolt);
        entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
        if (entity.getRemainingFireTicks() == 0) {
            entity.setSecondsOnFire(8);
        }
        entity.hurt(IMDamageTypes.TASTE_BUD_SHOCK.get(level), 20);
    }

}
