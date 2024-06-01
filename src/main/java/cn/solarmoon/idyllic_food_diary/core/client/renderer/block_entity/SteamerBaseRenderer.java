package cn.solarmoon.idyllic_food_diary.core.client.renderer.block_entity;

import cn.solarmoon.idyllic_food_diary.api.util.TextureRenderUtil;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SteamerBaseBlockEntity;
import cn.solarmoon.solarmoon_core.api.client.renderer.blockEntity.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SteamerBaseRenderer<E extends SteamerBaseBlockEntity> extends BaseBlockEntityRenderer<E> {

    public SteamerBaseRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(E base, float v, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        IFluidHandler tank = FluidUtil.getTank(base);
        if (tank == null || tank.getFluidInTank(0) == null) {
            return;
        }
        FluidStack fluidStack = tank.getFluidInTank(0);

        // 渲染流体过渡动画
        float zoom = 12f / 16f;
        int ticks = base.getTicks();
        float progress = ticks * 0.01f;
        float targetScale = FluidUtil.getScale(base.getTank());
        base.setLast(targetScale - (targetScale - base.getLast()) * (1 - progress));

        int targetColor = TextureRenderUtil.getColor(fluidStack);
        IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);

        poseStack.pushPose();
        float posB = 2f / 16f;
        poseStack.translate(posB, 0.15f, posB);
        poseStack.scale(zoom, base.getLast() * 0.75f, zoom);
        if (spriteLocation != null) {
            TextureRenderUtil.renderFluid(targetColor, 1, 0,
                    0, 0, 16, 16,
                    spriteLocation, poseStack, buffer, light);
        }
        poseStack.popPose();
    }

}
