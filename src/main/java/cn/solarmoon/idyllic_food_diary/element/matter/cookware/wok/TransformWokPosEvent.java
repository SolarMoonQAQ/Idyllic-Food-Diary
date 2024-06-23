package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.item_util.ItemStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TransformWokPosEvent {

    @SubscribeEvent
    public void trans1(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        InteractionHand hand = event.getHand();
        LocalPlayer player = Minecraft.getInstance().player;
        PoseStack poseStack = event.getPoseStack();
        boolean hasEmptyHand = player.getMainHandItem().isEmpty() || player.getOffhandItem().isEmpty();
        if (hasEmptyHand) {
            InteractionHand handHeld = null;
            if (player.getMainHandItem().is(IMItems.WOK.get())) handHeld = InteractionHand.MAIN_HAND;
            else if (player.getOffhandItem().is(IMItems.WOK.get())) handHeld = InteractionHand.OFF_HAND;
            if (handHeld != null && hand == handHeld) {
                poseStack.translate(hand == InteractionHand.MAIN_HAND ? -0.5 : 0.5, 0, 0);
            }
        }
    }

    public static void trans3(ItemStack stack, LivingEntity entity, ItemDisplayContext context, PoseStack poseStack) {
        if (stack.is(IMItems.WOK.get()) && (entity.getMainHandItem().isEmpty() || entity.getOffhandItem().isEmpty())) {
            if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                poseStack.translate(0.375, 0.1, 0.3);
            } else if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                poseStack.translate(-0.375, 0.1, 0.3);
            }
        }
    }

}
