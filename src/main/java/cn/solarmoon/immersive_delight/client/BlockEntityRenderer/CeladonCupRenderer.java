package cn.solarmoon.immersive_delight.client.BlockEntityRenderer;

import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.util.FluidHelper;
import cn.solarmoon.immersive_delight.util.FluidRenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class CeladonCupRenderer implements BlockEntityRenderer<TankBlockEntity> {

    public CeladonCupRenderer(BlockEntityRendererProvider.Context context) {
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void render(TankBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        IFluidHandler tank = FluidHelper.getTank(blockEntity);
        if (tank == null || tank.getFluidInTank(0) == null) {
            return;
        }
        FluidStack fluidStack = tank.getFluidInTank(0);

        VertexConsumer consumer = buffer.getBuffer(FluidRenderHelper.FluidTankRenderType.RESIZABLE);

        // 渲染过渡动画
        float zoom = 0.5f;
        int ticks = blockEntity.ticks;
        float progress = ticks * 0.1f;
        float targetScale = FluidHelper.getScale(blockEntity.tank);
        blockEntity.lastScale = targetScale - (targetScale - blockEntity.lastScale) * (1 - progress);
        poseStack.scale(zoom, blockEntity.lastScale * zoom, zoom);

        int targetColor = FluidRenderHelper.FluidRenderMap.getColor(fluidStack);

        FluidRenderHelper.Model3D model = FluidRenderHelper.getFluidModel(fluidStack, FluidRenderHelper.STAGES - 1);

        FluidRenderHelper.RenderResizableCuboid.renderCube(model,
                poseStack, consumer, targetColor,
                FluidRenderHelper.FluidRenderMap.calculateGlowLight(light, fluidStack));
    }

}
