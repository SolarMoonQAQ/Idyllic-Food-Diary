package cn.solarmoon.immersive_delight.common.fluid.core;

import cn.solarmoon.immersive_delight.api.common.fluid.BaseFluid;
import cn.solarmoon.immersive_delight.api.util.DamageUtil;
import cn.solarmoon.immersive_delight.common.registry.IMDamageTypes;
import cn.solarmoon.immersive_delight.common.registry.IMEffects;
import cn.solarmoon.immersive_delight.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Supplier;

public abstract class AbstractHotFluid {

    public static class FluidBlock extends BaseFluid.FluidBlock {

        public FluidBlock(Supplier<? extends FlowingFluid> supplier) {
            super(supplier);
        }

        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            super.entityInside(state, level, pos, entity);
            //效果
            if(entity instanceof LivingEntity living) {
                int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, living);
                if (living.hasEffect(MobEffects.FIRE_RESISTANCE) || enchantmentLevel >= 4) {
                    //给2级的十秒温暖效果 和 1级的十秒生命恢复
                    //生命恢复只能间断给予，不然会瞬满血，为了美观图标不可见
                    living.addEffect(new MobEffectInstance(IMEffects.Snug.get(), 200, 1));
                    if(!living.hasEffect(MobEffects.REGENERATION)) {
                        living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0, false, false, false));
                    }
                    //只在连火焰保护都没有的情况下造成伤害
                } else if (enchantmentLevel < 1) {
                    living.hurt(DamageUtil.getSimpleDamageSource(level, IMDamageTypes.scald), 1f);
                }
            }

            //先限定活体，等以后有煮东西配方了就删了
            if (entity instanceof LivingEntity) {
                //音效
                CompoundTag tag = entity.getPersistentData();
                if (!tag.contains("inHotFluid")) {
                    tag.putBoolean("inHotFluid", false);
                }
                boolean isInHotFluid = FluidUtil.isInHotFluid(entity);
                if (isInHotFluid && !tag.getBoolean("inHotFluid")) {
                    if (!level.isClientSide)
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1F, 1F);
                }
                tag.putBoolean("inHotFluid", isInHotFluid);
            }
        }

    }

    public static class Flowing extends BaseFluid.Flowing {
        public Flowing(Properties properties) {
            super(properties);
        }

        @Override
        public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
            super.animateTick(level, pos, state, randomSource);
            commonAnimate(level, pos, state, randomSource);
        }
    }

    public static class Source extends BaseFluid.Source {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
            super.animateTick(level, pos, state, randomSource);
            commonAnimate(level, pos, state, randomSource);
        }
    }

    public static class Bucket extends BaseFluid.Bucket {
        public Bucket(Supplier<? extends FlowingFluid> supplier) {
            super(supplier);
        }
    }

    public static void commonAnimate(Level level, BlockPos pos, FluidState state, RandomSource random) {

        //只在上表面生成粒子
        if(random.nextDouble() < 0.05 && !level.getFluidState(pos.above()).is(state.getType())) {
            level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY() + 2, pos.getZ(), 0, 0.07, 0);
        }

    }

}
