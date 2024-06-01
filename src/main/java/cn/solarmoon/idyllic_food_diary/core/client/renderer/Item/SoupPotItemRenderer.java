package cn.solarmoon.idyllic_food_diary.core.client.renderer.Item;

import cn.solarmoon.idyllic_food_diary.api.util.TextureRenderUtil;
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

public class SoupPotItemRenderer extends BaseItemRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        BlockItem item = ((BlockItem) itemStack.getItem());
        BlockState state = item.getBlock().defaultBlockState();
        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, overlay);

        FluidStack fluidStack = FluidUtil.getFluidStack(itemStack);
        IFluidHandlerItem stackTank = FluidUtil.getTank(itemStack);
        float zoom = 10f / 16f;
        float targetScale = FluidUtil.getScale(stackTank);
        int targetColor = TextureRenderUtil.getColor(fluidStack);
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);
        if (spriteLocation == null) return;
        poseStack.pushPose();
        float posB = 3f / 16f; //坐标偏移
        poseStack.translate(posB, 0.0625f, posB);
        poseStack.scale(zoom, targetScale * 0.9375f, zoom); //中间为最大高度
        TextureRenderUtil.renderFluid(targetColor, 1, 0,
                0, 0, 16, 16,
                spriteLocation, poseStack, buffer, light);
        poseStack.popPose();

        ItemStackHandler inventory = ContainerUtil.getInventory(itemStack);
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            poseStack.pushPose();
            poseStack.translate(0.5, 0.0625 * i + 0.0625, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, null, 0);
            poseStack.popPose();
        }
    }
}
