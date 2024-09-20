package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlockEntity
import cn.solarmoon.idyllic_food_diary.registry.client.IFDLayers
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBox
import cn.solarmoon.spark_core.api.phys.collision.FreeCollisionBoxRenderManager
import cn.solarmoon.spark_core.api.renderer.HandyBlockEntityRenderer
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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class SkewerRackBlockRenderer(context: BlockEntityRendererProvider.Context): HandyBlockEntityRenderer<SkewerRackBlockEntity>(context) {

    val main = context.bakeLayer(IFDLayers.SKEWER_RACK).getChild("main")

    init {
        main.setRotation(0f, -Math.PI.toFloat(), 0f)
    }

    companion object {
        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root
            val bb_main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -1.0F, -1.0F, 16.0F, 2.0F, 2.0F, CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F))
            return LayerDefinition.create(meshdefinition, 48, 48)
        }
    }

    override fun render(
        rack: SkewerRackBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val vertexConsumer = bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.fromNamespaceAndPath(IdyllicFoodDiary.MOD_ID, "textures/block/skewer_rack.png")))
        val direction = rack.blockState.getValue(IHorizontalFacingState.FACING)
        var light = LevelRenderer.getLightColor(rack.level!!, rack.blockPos)

        // 渲染上或下连接的栅栏
        rack.getConnectedValidFence()?.let {
            context.blockRenderDispatcher.renderSingleBlock(it.block.defaultBlockState(), poseStack, bufferSource, light, packedOverlay)
        }

        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot() + 180))
        poseStack.pushPose()
        val anim = rack.getData(SparkAttachments.ANIMTICKER)
        val timer = anim.timers[MillstoneBlockEntity.ANIM_ROTATION]!!
        var values = anim.fixedValues
        if (timer.isTiming) {
            poseStack.mulPose(Axis.XN.rotationDegrees(values.getOrDefault("rot", 0f) + partialTick * values.getOrDefault("velocity", 0f)))
        } else {
            poseStack.mulPose(Axis.XN.rotationDegrees(values.getOrDefault("rot", 0f)))
        }
        // 渲染架子
        main.render(poseStack, vertexConsumer, light, packedOverlay)
        // 渲染悬挂物
        poseStack.pushPose()
        val item = rack.inventory.getStackInSlot(0)
        val block = Block.byItem(item.item)
        poseStack.mulPose(Axis.YP.rotationDegrees(90f))
        poseStack.scale(0.5f, 0.5f, 0.5f)
        if (block == Blocks.AIR) {
            context.itemRenderer.renderStatic(rack.inventory.getStackInSlot(0), ItemDisplayContext.FIXED, light, packedOverlay, poseStack, bufferSource, rack.level, rack.blockPos.asLong().toInt())
        } else {
            var blockState = block.defaultBlockState()
            if (blockState.hasProperty(IBedPartState.PART) && blockState.renderShape != RenderShape.ENTITYBLOCK_ANIMATED) {
                poseStack.scale(1.5f, 1.5f, 1.5f)
                poseStack.pushPose()
                poseStack.translate(-0.5, -0.25, 0.0)
                context.blockRenderDispatcher.renderSingleBlock(blockState, poseStack, bufferSource, light, packedOverlay)
                poseStack.popPose()
                poseStack.pushPose()
                poseStack.translate(-0.5, -0.25, -1.0)
                context.blockRenderDispatcher.renderSingleBlock(blockState.setValue(IBedPartState.PART, BedPart.HEAD), poseStack, bufferSource, light, packedOverlay)
                poseStack.popPose()
            } else {
                poseStack.translate(-0.5, -0.5, -0.5)
                if (blockState.hasProperty(IHorizontalFacingState.FACING)) blockState = blockState.setValue(IHorizontalFacingState.FACING, rack.blockState.getValue(IHorizontalFacingState.FACING))
                context.blockRenderDispatcher.renderSingleBlock(blockState, poseStack, bufferSource, light, packedOverlay)
            }
        }
        poseStack.popPose()
        poseStack.popPose()

        // debug
        val structure = (rack.structure ?: return).takeIf { it.leftTerminal.blockPos == rack.blockPos } ?: return
        structure.allRack.forEach {
            FreeCollisionBoxRenderManager("${it.blockPos} - RackDebug", FreeCollisionBox(it.blockPos.center, Vec3(1.0, 1.0, 1.0))).start()
        }
    }

    override fun shouldRender(blockEntity: SkewerRackBlockEntity, cameraPos: Vec3): Boolean {
        return true
    }

    override fun getRenderBoundingBox(blockEntity: SkewerRackBlockEntity): AABB {
        return super.getRenderBoundingBox(blockEntity).inflate(3.0)
    }

}