package cn.solarmoon.idyllic_food_diary.element.matter.durian;

import cn.solarmoon.idyllic_food_diary.registry.common.IMEntityTypes;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.util.DropUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * 可以扔的榴莲实体
 * 砸中造成伤害，裂成产物，必然被删除
 */
public class DurianEntity extends AbstractArrow {

    public DurianEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public DurianEntity(Level level, LivingEntity entity) {
        super(IMEntityTypes.DURIAN_ENTITY.get(), entity, level);
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        Level level = this.level();
        DropUtil.summonDrop(IMItems.DURIAN_FLESH.get(), level, hit.getLocation(), 2, 4);
        DropUtil.summonDrop(IMItems.DURIAN_SHELL.get(), level, hit.getLocation(), 2);
        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.AXE_STRIP;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public ItemStack getItem() {
        return IMItems.DURIAN.get().getDefaultInstance();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        }
    }

    private void makeParticle(int i) {
        for(int j = 0; j < i; ++j) {
            this.level().addParticle(ParticleTypes.CRIT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 1,1,1);
        }
    }

}
