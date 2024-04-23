package cn.solarmoon.idyllic_food_diary.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * 温暖
 * 增加回血比例
 * 每级增加百分之二十
 */
public class SnugEffect extends MobEffect {

    public SnugEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xF4A460);
    }

    /**
     * 具体实现在event中
     */
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
    }

    /**
     * 保持tick
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
