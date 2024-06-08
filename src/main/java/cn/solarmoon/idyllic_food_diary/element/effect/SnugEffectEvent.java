package cn.solarmoon.idyllic_food_diary.element.effect;

import cn.solarmoon.idyllic_food_diary.registry.common.IMEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

public class SnugEffectEvent {

    @SubscribeEvent
    public void extraHeal(LivingHealEvent event) {
        LivingEntity living = event.getEntity();
        if (living.hasEffect(IMEffects.SNUG.get())) {
            int amplifier = Objects.requireNonNull(living.getEffect(IMEffects.SNUG.get())).getAmplifier();
            // 计算额外的回血量，每级增加百分之二十
            float extraHealAmount = event.getAmount() * 0.2F * (amplifier + 1);
            // 增加额外的回血量
            event.setAmount(extraHealAmount + event.getAmount());
        }
    }

}
