package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.feature.hug_item.IHuggableItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

import static cn.solarmoon.idyllic_food_diary.feature.hug_item.PreventArmShowEvent.HUG;

public class WokItem extends BlockItem implements ITankTileItem, IHuggableItem {

    public WokItem() {
        super(IMBlocks.WOK.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getMainHandItem().isEmpty() || entityLiving.getOffhandItem().isEmpty()) {
                    return HUG;
                }
                return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if (player.getMainHandItem().isEmpty() || player.getOffhandItem().isEmpty()) {
                    poseStack.translate(arm == HumanoidArm.RIGHT ? -0.5 : 0.5, 0, 0.1);
                    return false;
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WokItemRenderer();
            }

        });
    }

    @Override
    public void transform3rdPDisplay(ItemStack stack, LivingEntity entity, ItemDisplayContext context, PoseStack poseStack) {
        if (entity.getMainHandItem().isEmpty() || entity.getOffhandItem().isEmpty()) {
            if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                poseStack.translate(0.365, 0.1, 0.3);
            } else if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                poseStack.translate(-0.365, 0.1, 0.3);
            }
        }
    }

}
