package cn.solarmoon.idyllic_food_diary.core.common.effect;

import cn.solarmoon.idyllic_food_diary.api.util.namespace.NETList;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMPacks;
import cn.solarmoon.solarmoon_core.api.util.UUIDGetter;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

/**
 * 健步如飞
 * 加速，并且提高抬升高度，和跳跃高度
 */
public class FleetOfFootEffect extends MobEffect {

    public FleetOfFootEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), String.valueOf(UUIDGetter.get(ForgeMod.STEP_HEIGHT_ADDITION.get())), 0.5f, AttributeModifier.Operation.ADDITION);
    }

    /**
     * 保持tick
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
