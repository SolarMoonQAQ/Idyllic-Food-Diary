package cn.solarmoon.immersive_delight.common.effect;

import cn.solarmoon.immersive_delight.common.registry.IMEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class TeaAromaEffect extends MobEffect {

    public TeaAromaEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x008B45);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity.getRandom().nextInt(1000) == 500) {
            entity.addEffect(new MobEffectInstance(IMEffects.REFRESHMENT.get(), 10, 0));
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
