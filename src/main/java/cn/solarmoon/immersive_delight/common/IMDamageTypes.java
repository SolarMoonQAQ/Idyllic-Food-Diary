package cn.solarmoon.immersive_delight.common;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * 伤害类型
 * 这个竟然能直接注册
 */
public class IMDamageTypes {

    //烫伤
    public static final ResourceKey<DamageType> scald = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ImmersiveDelight.MOD_ID, "scald"));
    //榴莲砸伤
    public static final ResourceKey<DamageType> falling_durian = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ImmersiveDelight.MOD_ID, "falling_durian"));

    public DamageSource getFallDamageSource(Entity p_254036_) {
        return p_254036_.damageSources().anvil(p_254036_);
    }

    public static DamageSource getSimpleDamageSource(Level level, ResourceKey<DamageType> type) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type));
    }

}
