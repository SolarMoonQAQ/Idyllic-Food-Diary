package cn.solarmoon.idyllic_food_diary.feature.hug_item;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PreventArmShowEvent {

    public static final HumanoidModel.ArmPose HUG = HumanoidModel.ArmPose.create("HUG", true, (model, entity, arm) -> {
        model.leftArm.xRot = -((float) Math.PI / 5F);
        model.leftArm.yRot = 0.0F;
        model.rightArm.xRot = -((float) Math.PI / 5F);
        model.rightArm.yRot = 0.0F;
    });

    @SubscribeEvent
    public void prevent(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        boolean f1 = player.getMainHandItem().getItem() instanceof IHuggableItem && player.getOffhandItem().isEmpty();
        boolean f2 = player.getOffhandItem().getItem() instanceof IHuggableItem && player.getMainHandItem().isEmpty();
        if (f1 || f2) {
            event.setCanceled(true);
        }
    }

}
