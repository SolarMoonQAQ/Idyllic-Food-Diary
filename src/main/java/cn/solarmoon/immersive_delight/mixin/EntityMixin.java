package cn.solarmoon.immersive_delight.mixin;

import cn.solarmoon.immersive_delight.api.util.FluidUtil;
import cn.solarmoon.immersive_delight.common.registry.IMParticles;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract FluidType getEyeInFluidType();

    @Shadow private Level level;

    @Shadow public abstract BlockPos getOnPos();

    @Shadow protected abstract void waterSwimSound();

    @Shadow protected abstract SoundEvent getSwimSplashSound();

    @Shadow protected abstract SoundEvent getSwimHighSpeedSplashSound();

    @Shadow @Final
    protected RandomSource random;

    @Shadow public abstract void playSound(SoundEvent p_19938_, float p_19939_, float p_19940_);

    @Shadow public abstract double getY();

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract Level level();

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Shadow public abstract void gameEvent(GameEvent p_146851_);

    /**
     * 使得模组液体具有水的音效（完全进入进出时的音效）
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        //第一次完全进入/出水判别 (模组液体)
        //这个限制 活体
        boolean isFullInWater = FluidUtil.IMFluidsMatch(getEyeInFluidType());
        Entity entity = ((Entity)(Object)this);
        if (entity instanceof LivingEntity) {
            CompoundTag tag = entity.getPersistentData();
            if (!tag.contains("fullInFluid")) {
                tag.putBoolean("fullInFluid", false);
            }
            if (isFullInWater && !tag.getBoolean("fullInFluid")) {
                level.playSound(null, this.getOnPos(), SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.PLAYERS, 1f, 1f);
            } else if (!isFullInWater && tag.getBoolean("fullInFluid")) {
                level.playSound(null, this.getOnPos(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 1f, 1f);
            }
            tag.putBoolean("fullInFluid", isFullInWater);
        }
    }

    /**
     * 使得模组液体具有水的粒子
     * 任何实体都应用
     */
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick2(CallbackInfo ci) {
        Entity entity = ((Entity)(Object)this);
        boolean isInFluid = FluidUtil.isInFluid(entity);
        //游泳音效
        if(isInFluid) {
            this.waterSwimSound();
        }
        CompoundTag tag = entity.getPersistentData();
        if(!tag.contains("inFluid")) {
            tag.putBoolean("inFluid", false);
        }
        // 刚刚进入水中
        if (isInFluid && !tag.getBoolean("inFluid")) {
            _1_20_1_forge$particle();
        }
        tag.putBoolean("inFluid", isInFluid);
    }

    @Unique
    protected void _1_20_1_forge$particle() {
        Entity entity = ((Entity)(Object)this);
        float f = entity == (Object)this ? 0.2F : 0.9F;
        Vec3 vec3 = entity.getDeltaMovement();
        float f1 = Math.min(1.0F, (float)Math.sqrt(vec3.x * vec3.x * (double)0.2F + vec3.y * vec3.y + vec3.z * vec3.z * (double)0.2F) * f);
        if (f1 < 0.25F) {
            this.playSound(this.getSwimSplashSound(), f1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        } else {
            this.playSound(this.getSwimHighSpeedSplashSound(), f1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        }

        float f2 = (float) Mth.floor(this.getY());

        for(int i = 0; (float)i < 1.0F + this.dimensions.width * 20.0F; ++i) {
            double d0 = (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.dimensions.width;
            double d1 = (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.dimensions.width;
            ParticleOptions particleBubble = ParticleTypes.BUBBLE;
            if (entity.isInFluidType(IMFluids.HotMilk.FLUID_TYPE.get())) {
                particleBubble = IMParticles.HOT_MILK_BUBBLE.get();
            }
            this.level().addParticle(particleBubble, this.getX() + d0, f2 + 1.0F, this.getZ() + d1, vec3.x, vec3.y - this.random.nextDouble() * (double)0.2F, vec3.z);
        }

        for(int j = 0; (float)j < 1.0F + this.dimensions.width * 20.0F; ++j) {
            double d2 = (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.dimensions.width;
            double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.dimensions.width;
            ParticleOptions particleSplash = ParticleTypes.SPLASH;
            if (entity.isInFluidType(IMFluids.HotMilk.FLUID_TYPE.get())) {
                particleSplash = IMParticles.HOT_MILK_SPLASH.get();
            }
            this.level().addParticle(particleSplash, this.getX() + d2, f2 + 1.0F, this.getZ() + d3, vec3.x, vec3.y, vec3.z);
        }

        this.gameEvent(GameEvent.SPLASH);
    }

}





