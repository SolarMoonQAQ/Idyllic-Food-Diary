package cn.solarmoon.idyllic_food_diary.mixin;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.rolling_pin.RollingPinItem;
import cn.solarmoon.idyllic_food_diary.feature.hug_item.IHuggableItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    public void renderArmWithItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, HumanoidArm arm, PoseStack poseStack, MultiBufferSource buffer, int l, CallbackInfo ci) {
        if (stack.getItem() instanceof IHuggableItem huggable) {
            huggable.transform3rdPDisplay(stack, entity, context, poseStack);
        }
    }

}
