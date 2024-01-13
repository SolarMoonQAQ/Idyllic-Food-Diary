package cn.solarmoon.immersive_delight.client.BlockEntityRenderer;

import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.CupEntityBlock;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.CupBlockEntity;
import cn.solarmoon.immersive_delight.common.entity_blocks.abstract_blocks.entities.TankBlockEntity;
import cn.solarmoon.immersive_delight.util.FluidHelper;
import cn.solarmoon.immersive_delight.util.FluidRenderHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CeladonCupRenderer implements BlockEntityRenderer<TankBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CeladonCupRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
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

        // 渲染流体过渡动画
        float zoom = 3/16f;
        int ticks = blockEntity.ticks;
        float progress = ticks * 0.1f;
        float targetScale = FluidHelper.getScale(blockEntity.tank);
        blockEntity.lastScale = targetScale - (targetScale - blockEntity.lastScale) * (1 - progress);

        int targetColor = FluidRenderHelper.FluidRenderMap.getColor(fluidStack);

        FluidRenderHelper.Model3D model = FluidRenderHelper.getFluidModel(fluidStack, FluidRenderHelper.STAGES - 1);

        poseStack.pushPose();
        float posB = 6.5f/16f;
        poseStack.translate(posB, 0.1f, posB);
        poseStack.scale(zoom, blockEntity.lastScale * 0.185f, zoom);
        FluidRenderHelper.RenderResizableCuboid.renderCube(model,
                poseStack, consumer, targetColor,
                FluidRenderHelper.FluidRenderMap.calculateGlowLight(light, fluidStack));
        poseStack.popPose();

        //渲染物品
        if (blockEntity instanceof CupBlockEntity cup) {
            //让光度和环境一致
            light = blockEntity.getLevel() != null ? LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above()) : 15728880;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.15, 0.5);
            //根据面朝方向决定旋转角
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getBlockState().getValue(CupEntityBlock.FACING).toYRot() + 45));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderStatic(cup.getItem(), ItemDisplayContext.GROUND, light, overlay, poseStack, buffer, blockEntity.getLevel(), (int) blockEntity.getBlockPos().asLong());
            poseStack.popPose();
        }
    }

}
