package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareTileRenderer
import cn.solarmoon.idyllic_food_diary.registry.client.IFDLayers
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.renderer.HandyBlockEntityRenderer
import cn.solarmoon.spark_core.api.renderer.TextureRenderHelper
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

class MillstoneBlockRenderer(context: BlockEntityRendererProvider.Context): HandyBlockEntityRenderer<MillstoneBlockEntity>(context) {

    val wheel = context.bakeLayer(IFDLayers.MILLSTONE).getChild("wheel")
    val main = context.bakeLayer(IFDLayers.MILLSTONE).getChild("main")

    init {
        wheel.setRotation(0f, 0f, -Math.PI.toFloat())
    }

    companion object {
        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root;

            val wheel = partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(61, 5).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 6.0F, 10.0F, CubeDeformation(0.0F))
            .texOffs(56, 53).addBox(5.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

            val main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 34).addBox(7.0F, 0.0F, 0.0F, 1.0F, 3.0F, 16.0F, CubeDeformation(0.0F))
            .texOffs(0, 53).addBox(1.0F, 0.0F, 0.0F, 6.0F, 3.0F, 1.0F, CubeDeformation(0.0F))
                .texOffs(40, 53).addBox(1.0F, 0.0F, -3.0F, 1.0F, 3.0F, 3.0F, CubeDeformation(0.0F))
                .texOffs(48, 53).addBox(-2.0F, 0.0F, -3.0F, 1.0F, 3.0F, 3.0F, CubeDeformation(0.0F))
                .texOffs(34, 34).addBox(-7.0F, 0.0F, 15.0F, 14.0F, 3.0F, 1.0F, CubeDeformation(0.0F))
                .texOffs(40, 15).addBox(-8.0F, 0.0F, 0.0F, 1.0F, 3.0F, 16.0F, CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, 0.0F, 1.0F, 14.0F, 1.0F, 14.0F, CubeDeformation(0.0F))
                .texOffs(14, 53).addBox(-7.0F, 0.0F, 0.0F, 6.0F, 3.0F, 1.0F, CubeDeformation(0.0F))
                .texOffs(28, 53).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, CubeDeformation(0.0F))
                .texOffs(0, 15).addBox(-5.0F, 1.0F, 3.0F, 10.0F, 2.0F, 10.0F, CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }

        @JvmStatic
        fun renderAnimatedFluid(mill: MillstoneBlockEntity, side: Direction, partialTicks: Float, poseStack: PoseStack, buffer: MultiBufferSource, light: Int) {
            val tank = mill.tanks[1]
            val anim = mill.getData(SparkAttachments.ANIMTICKER)
            val fluidStack = tank.getFluidInTank(0)
            val targetColor = TextureRenderHelper.getColor(fluidStack)
            val fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.fluid)
            val spriteLocation = fluidAttributes.getStillTexture(fluidStack)

            if (spriteLocation != null) {
                // 盘中液体
                poseStack.pushPose()
                poseStack.translate(-0.5, 0.0, -0.5)
                AnimHelper.Fluid.renderAnimatedFluid(mill, side, 14 / 16f, (2 - 0.02f) / 16f, 1 / 16.0, partialTicks, poseStack, buffer, light)
                poseStack.popPose()
                poseStack.pushPose()
                poseStack.translate(-0.5, 0.0, -1.0)
                AnimHelper.Fluid.renderAnimatedFluid(mill, side, 2 / 16f, 2 / 16f, 1 / 16.0, partialTicks, poseStack, buffer, light)
                poseStack.translate(0.0, 0.0, -2/16.0)
                AnimHelper.Fluid.renderAnimatedFluid(mill, side, 2 / 16f, 2 / 16f, 1 / 16.0, partialTicks, poseStack, buffer, light)
                poseStack.popPose()

                // 龙头
                val tick = anim.fixedValues.getOrDefault("flow", 0f)
                val maxTick = anim.fixedValues.getOrDefault("flowMax", 10f)
                var delta = partialTicks
                if (tick > 0) {
                    val h = 2 * FluidHandlerHelper.getScale(tank)
                    if (tick >= maxTick) delta = 0f
                    if (!mill.grind.flowing) delta = -partialTicks
                    poseStack.pushPose()
                    poseStack.translate(-0.5f, (1 + h) / 16f, -4 / 16f)
                    poseStack.mulPose(Axis.XN.rotationDegrees(180f))
                    TextureRenderHelper.render(spriteLocation, 0, 0, 2, (16 * (tick / maxTick)).toInt(), 2 / 16F, (tick + delta) * (16 + h) / 16f / maxTick, targetColor, 1.0F, 0, poseStack, buffer, light)
                    poseStack.popPose()
                }
            }
        }
    }

    override fun render(
        millstone: MillstoneBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val vertexConsumer = bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, "textures/block/millstone.png")))
        val level = millstone.getLevel()
        if (level == null) return
        val direction = millstone.blockState.getValue(IHorizontalFacingState.FACING)
        var light = LevelRenderer.getLightColor(millstone.level!!, millstone.blockPos)

        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot() + 180));
        poseStack.pushPose();
        val anim = millstone.getData(SparkAttachments.ANIMTICKER)
        val timer = anim.timers[MillstoneBlockEntity.ANIM_ROTATION]!!
        var values = anim.fixedValues
        if (timer.isTiming) {
            poseStack.mulPose(Axis.YP.rotationDegrees(values.getOrDefault("rot", 0f) + partialTick * values.getOrDefault("velocity", 0f)))
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(values.getOrDefault("rot", 0f)))
        }
        wheel.render(poseStack, vertexConsumer, light, packedOverlay)
        poseStack.popPose()

        poseStack.pushPose()
        main.render(poseStack, vertexConsumer, light, packedOverlay)
        poseStack.popPose()

        renderAnimatedFluid(millstone, Direction.DOWN, partialTick, poseStack, bufferSource, light)
    }

    override fun shouldRender(blockEntity: MillstoneBlockEntity, cameraPos: Vec3): Boolean {
        return true
    }

    override fun shouldRenderOffScreen(blockEntity: MillstoneBlockEntity): Boolean {
        return true
    }

    override fun getRenderBoundingBox(blockEntity: MillstoneBlockEntity): AABB {
        return super.getRenderBoundingBox(blockEntity).inflate(3.0)
    }

}