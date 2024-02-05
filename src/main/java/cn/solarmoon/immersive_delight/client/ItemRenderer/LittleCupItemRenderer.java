package cn.solarmoon.immersive_delight.client.ItemRenderer;

import cn.solarmoon.immersive_delight.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.immersive_delight.util.ContainerUtil;
import cn.solarmoon.immersive_delight.api.util.FluidUtil;
import cn.solarmoon.immersive_delight.util.FluidRenderAnotherUtil;
import cn.solarmoon.immersive_delight.util.FluidRenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
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

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        //渲染物品
        poseStack.pushPose();
        poseStack.translate(0.5, 0.15, 0.5);
        //根据面朝方向决定旋转角
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        ItemStackHandler inventory = ContainerUtil.getInventory(stack);
        itemRenderer.renderStatic(inventory.getStackInSlot(0), ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, null, 0);
        poseStack.popPose();

        FluidStack fluidStack = FluidUtil.getFluidStack(stack);
        IFluidHandlerItem stackTank = FluidUtil.getTank(stack);
        float zoom = 0.4f;
        float targetScale = FluidUtil.getScale(stackTank);

        int targetColor = FluidRenderUtil.FluidRenderMap.getColor(fluidStack);
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);
        if (spriteLocation == null) return;

        poseStack.pushPose();
        poseStack.translate(0.3, 0, 0.3);
        poseStack.scale(zoom, 1, zoom);
        FluidRenderAnotherUtil.renderFluid(targetScale * 0.29f, targetColor, 0, spriteLocation,
                poseStack, buffer, light, overlay);
        poseStack.popPose();

    }

}
