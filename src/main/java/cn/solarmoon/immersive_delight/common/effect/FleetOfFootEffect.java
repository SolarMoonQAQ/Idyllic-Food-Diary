package cn.solarmoon.immersive_delight.common.effect;

import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * 健步如飞
 * 加速，并且提高抬升高度，和跳跃高度
 */
public class FleetOfFootEffect extends MobEffect {

    public FleetOfFootEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFF00);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    /**
     * 提高抬升高度
     * 1级0.5格
     */
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setMaxUpStep( 0.6f + (amplifier + 1) * 0.5f );
    }

    /**
     * 移除时恢复正常
     * 需客户端同步
     */
    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amplifier) {
        super.removeAttributeModifiers(entity, map, amplifier);
        IMPacks.CLIENT_PACK.getSender().send(NETList.SYNC_UP_STEP, 0.6f);
        entity.setMaxUpStep(0.6f);
    }

    /**
     * 保持tick
     */
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}
