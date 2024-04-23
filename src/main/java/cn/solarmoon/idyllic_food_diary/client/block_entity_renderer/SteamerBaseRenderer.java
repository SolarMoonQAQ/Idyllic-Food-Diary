package cn.solarmoon.idyllic_food_diary.client.block_entity_renderer;

import cn.solarmoon.idyllic_food_diary.common.block_entity.SteamerBaseBlockEntity;
import cn.solarmoon.idyllic_food_diary.util.FluidRenderUtil;
import cn.solarmoon.solarmoon_core.api.client.blockEntityRenderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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

        VertexConsumer consumer = buffer.getBuffer(FluidRenderUtil.FluidTankRenderType.RESIZABLE);

        // 渲染流体过渡动画
        float zoom = 12f / 16f;
        int ticks = base.getTicks();
        float progress = ticks * 0.01f;
        float targetScale = FluidUtil.getScale(base.getTank());
        base.setLast(targetScale - (targetScale - base.getLast()) * (1 - progress));

        int targetColor = FluidRenderUtil.FluidRenderMap.getColor(fluidStack);

        FluidRenderUtil.Model3D model = FluidRenderUtil.getFluidModel(fluidStack, FluidRenderUtil.STAGES - 1);

        poseStack.pushPose();
        float posB = 2f / 16f;
        poseStack.translate(posB, 0.125f, posB);
        poseStack.scale(zoom, base.getLast() * 0.8125f, zoom);
        FluidRenderUtil.RenderResizableCuboid.renderCube(model,
                poseStack, consumer, targetColor,
                FluidRenderUtil.FluidRenderMap.calculateGlowLight(light, fluidStack));
        poseStack.popPose();
    }

}
