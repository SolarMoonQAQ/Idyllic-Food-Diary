package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.feature.spice.*;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    LivingEntity entity = (LivingEntity) (Object) this;

    @Shadow public abstract boolean addEffect(MobEffectInstance p_21165_);

    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(Level level, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        Either<List<MobEffectInstance>, Consumer<LivingEntity>> either = Flavor.output(stack);
        either.right().ifPresent(l -> l.accept(entity));
        either.left().ifPresent(l -> {
            l.forEach(this::addEffect);
        });
    }

}
