package cn.solarmoon.idyllic_food_diary.core.client.renderer.Item;

import cn.solarmoon.idyllic_food_diary.api.util.FluidRenderUtil;
import cn.solarmoon.solarmoon_core.api.client.renderer.Item.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;

public class LittleCupItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        // 物品
        poseStack.pushPose();
        poseStack.translate(0.5, 0.15, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        ItemStackHandler inventory = ContainerUtil.getInventory(stack);
        itemRenderer.renderStatic(inventory.getStackInSlot(0), ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        poseStack.popPose();

        // 液体
        FluidStack fluidStack = FluidUtil.getFluidStack(stack);
        IFluidHandlerItem stackTank = FluidUtil.getTank(stack);
        float zoom = 3 / 16f;
        float targetScale = FluidUtil.getScale(stackTank);
        int targetColor = FluidRenderUtil.getColor(fluidStack);
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);
        if (spriteLocation == null) return;
        poseStack.pushPose();
        float posB = 6.5f / 16f;
        poseStack.translate(posB, 0.09375f, posB);
        poseStack.scale(zoom, targetScale * 0.1875f, zoom);
        FluidRenderUtil.renderFluid(targetColor, 0, spriteLocation,
                poseStack, buffer, light, overlay);
        poseStack.popPose();

    }

}
