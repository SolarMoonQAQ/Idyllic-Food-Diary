package cn.solarmoon.idyllic_food_diary.client.Item_renderer;

import cn.solarmoon.idyllic_food_diary.util.FluidRenderAnotherUtil;
import cn.solarmoon.idyllic_food_diary.util.FluidRenderUtil;
import cn.solarmoon.solarmoon_core.api.client.ItemRenderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import com.mojang.blaze3d.vertex.PoseStack;
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

public class SteamerBaseItemRenderer extends BaseItemRenderer {

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {

        BlockItem item = ((BlockItem) stack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        FluidStack fluidStack = FluidUtil.getFluidStack(stack);
        IFluidHandlerItem stackTank = FluidUtil.getTank(stack);
        float zoom = 1.5f;
        float targetScale = FluidUtil.getScale(stackTank);

        int targetColor = FluidRenderUtil.FluidRenderMap.getColor(fluidStack);
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);
        if (spriteLocation == null) return;

        poseStack.pushPose();
        float posB = 3.5f / 16f;
        poseStack.translate(-posB, 0.0625, -posB);
        poseStack.scale(zoom, 1, zoom);
        FluidRenderAnotherUtil.renderFluid(targetScale, targetColor, 0, spriteLocation,
                poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

}
