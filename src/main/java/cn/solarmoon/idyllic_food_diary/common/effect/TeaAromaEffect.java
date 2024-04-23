package cn.solarmoon.idyllic_food_diary.common.effect;

import cn.solarmoon.idyllic_food_diary.common.registry.IMEffects;
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
        if (entity.getRandom().nextInt(1000) == 500 && !entity.level().isClientSide) {
            entity.addEffect(new MobEffectInstance(IMEffects.REFRESHMENT.get(), 20, 0));
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
