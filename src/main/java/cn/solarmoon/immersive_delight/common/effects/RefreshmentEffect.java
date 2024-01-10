package cn.solarmoon.immersive_delight.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

/**
 * 神清气爽
 * 可以抵消所有负面效果
 */
public class RefreshmentEffect extends MobEffect {

    public RefreshmentEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    /**
     * 抵消所有负面效果
     */
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        Collection<MobEffectInstance> effects = entity.getActiveEffects();
        for(var effect : effects) {
            if(effect.getEffect().isBeneficial()) continue;
            entity.removeEffect(effect.getEffect());
        }
    }

    /**
     * 保持tick
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
