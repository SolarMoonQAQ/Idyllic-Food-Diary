package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.feature.hug_item.IHuggableItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.item_util.IContainerItem;
import cn.solarmoon.solarmoon_core.api.item_util.ITankItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.IItemRendererProvider;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static cn.solarmoon.idyllic_food_diary.feature.hug_item.PreventArmShowEvent.HUG;

public class CookingPotItem extends BlockItem implements ITankTileItem, IHuggableItem {

    public CookingPotItem() {
        super(IMBlocks.COOKING_POT.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getMainHandItem().isEmpty() || entityLiving.getOffhandItem().isEmpty()) {
                    return HUG;
                }
                return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if (player.getMainHandItem().isEmpty() || player.getOffhandItem().isEmpty()) {
                    poseStack.translate(arm == HumanoidArm.RIGHT ? -0.55 : 0.55, 0, 0.1);
                    return false;
                }
                return IClientItemExtensions.super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new CookingPotItemRenderer();
            }
        });
    }

    @Override
    public void transform3rdPDisplay(ItemStack stack, LivingEntity entity, ItemDisplayContext context, PoseStack poseStack) {
        if (entity.getMainHandItem().isEmpty() || entity.getOffhandItem().isEmpty()) {
            if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                poseStack.translate(0.365, 0.2, 0.2);
            } else if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                poseStack.translate(-0.365, 0.2, 0.2);
            }
        }
    }

}
