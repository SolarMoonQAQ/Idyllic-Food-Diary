package cn.solarmoon.idyllic_food_diary.core.client.Item_renderer;

import cn.solarmoon.idyllic_food_diary.api.util.FluidRenderUtil;
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
        float zoom = 12f / 16f;
        float targetScale = FluidUtil.getScale(stackTank);

        int targetColor = FluidRenderUtil.getColor(fluidStack);
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);
        if (spriteLocation == null) return;

        poseStack.pushPose();
        float posB = 2f / 16f;
        poseStack.translate(posB, 0.15f, posB);
        poseStack.scale(zoom, targetScale * 0.75f, zoom);
        FluidRenderUtil.renderFluid(targetColor, 0, spriteLocation,
                poseStack, buffer, light, overlay);
        poseStack.popPose();
    }

}
