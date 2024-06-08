package cn.solarmoon.idyllic_food_diary.element.matter.fluid;

import cn.solarmoon.idyllic_food_diary.registry.common.IMDamageTypes;
import cn.solarmoon.idyllic_food_diary.registry.common.IMEffects;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.util.FluidTypeUtil;
import cn.solarmoon.solarmoon_core.api.common.fluid.BaseFluid;
import cn.solarmoon.solarmoon_core.api.common.registry.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class AbstractHotFluid {

    private final FluidEntry fluidEntry;

    public AbstractHotFluid(FluidEntry fluidEntry) {
        this.fluidEntry = fluidEntry;
    }

    public class FluidBlock extends BaseFluid.FluidBlock {

        public FluidBlock() {
            super(fluidEntry::getStill);
        }

        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            super.entityInside(state, level, pos, entity);
            //效果
            if(entity instanceof LivingEntity living) {
                int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, living);
                //良好的温泉体验需要：火焰保护等级之和大于等于4 / 或是有火焰保护药水 / 或是穿着浴衣
                if (living.hasEffect(MobEffects.FIRE_RESISTANCE) || enchantmentLevel >= 4 || living.getItemBySlot(EquipmentSlot.CHEST).is(IMItems.BATHROBE.get())) {
                    //给2级的十秒温暖效果 和 1级的十秒生命恢复
                    //生命恢复只能间断给予，不然会瞬满血，为了美观图标不可见
                    living.addEffect(new MobEffectInstance(IMEffects.SNUG.get(), 200, 1));
                    if(!living.hasEffect(MobEffects.REGENERATION)) {
                        living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0, false, false, false));
                    }
                    //只在连火焰保护都没有的情况下造成伤害
                } else if (enchantmentLevel < 1) {
                    living.hurt(IMDamageTypes.SCALD.get(level), 1f);
                }
            }

            //先限定活体，等以后有煮东西配方了就删了
            if (entity instanceof LivingEntity) {
                //音效
                CompoundTag tag = entity.getPersistentData();
                if (!tag.contains("inHotFluid")) {
                    tag.putBoolean("inHotFluid", false);
                }
                boolean isInHotFluid = FluidTypeUtil.isInHotFluid(entity);
                if (isInHotFluid && !tag.getBoolean("inHotFluid")) {
                    if (!level.isClientSide)
                        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1F, 1F);
                }
                tag.putBoolean("inHotFluid", isInHotFluid);
            }
        }

    }

    public class Flowing extends BaseFluid.Flowing {
        public Flowing() {
            super(makeProperties(fluidEntry));
        }

        @Override
        public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
            super.animateTick(level, pos, state, randomSource);
            commonAnimate(level, pos, state, randomSource);
        }
    }

    public class Source extends BaseFluid.Source {
        public Source() {
            super(makeProperties(fluidEntry));
        }

        @Override
        public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
            super.animateTick(level, pos, state, randomSource);
            commonAnimate(level, pos, state, randomSource);
        }
    }

    public class Bucket extends BaseFluid.Bucket {
        public Bucket() {
            super(fluidEntry.getStillObject());
        }
    }

    private ForgeFlowingFluid.Properties makeProperties(FluidEntry fluidEntry) {
        return new ForgeFlowingFluid.Properties(
                fluidEntry.getTypeObject(),
                fluidEntry.getStillObject(),
                fluidEntry.getFlowingObject()
        )
                .block(fluidEntry.getBlockObject())
                .bucket(fluidEntry.getBucketObject());
    }

    public static void commonAnimate(Level level, BlockPos pos, FluidState state, RandomSource random) {

        //只在上表面生成粒子
        if(random.nextDouble() < 0.05 && !level.getFluidState(pos.above()).is(state.getType())) {
            level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY() + 2, pos.getZ(), 0, 0.07, 0);
        }

    }

}
