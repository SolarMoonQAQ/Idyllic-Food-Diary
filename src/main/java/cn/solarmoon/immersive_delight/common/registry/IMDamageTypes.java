package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.registry.object.DamageTypeEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

/**
 * 伤害类型
 * 这个竟然能直接注册
 */
public class IMDamageTypes {

    //烫伤
    public static final DamageTypeEntry SCALD = ImmersiveDelight.REGISTRY.damageType()
            .id("scald")
            .build();

    //榴莲砸伤
    public static final DamageTypeEntry FALLING_DURIAN = ImmersiveDelight.REGISTRY.damageType()
            .id("falling_durian")
            .build();


}
