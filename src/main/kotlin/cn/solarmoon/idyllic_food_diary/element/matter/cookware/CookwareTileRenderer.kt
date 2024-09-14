package cn.solarmoon.idyllic_food_diary.element.matter.cookware

import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.renderer.HandyBlockEntityRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

abstract class CookwareTileRenderer<E: BlockEntity>(context: BlockEntityRendererProvider.Context): HandyBlockEntityRenderer<E>(context) {

    abstract fun renderThis(blockEntity: E, partialTick: Float, poseStack: PoseStack, bufferSource: MultiBufferSource, packedLight: Int, packedOverlay: Int)

    override fun render(
        blockEntity: E,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val state = blockEntity.blockState
        val block = state.block
        if (block is IBuiltInStove && block.isNestedInStove(state)) {
            val direction = state.getValue(IHorizontalFacingState.FACING);
            block.translateContent(direction, poseStack, bufferSource, packedLight, packedOverlay)
        }
        context.blockRenderDispatcher.renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay)
        renderThis(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay)
    }

    override fun shouldRender(blockEntity: E, cameraPos: Vec3): Boolean {
        return true
    }

    override fun shouldRenderOffScreen(blockEntity: E): Boolean {
        return true
    }

    override fun getRenderBoundingBox(blockEntity: E): AABB {
        return super.getRenderBoundingBox(blockEntity).inflate(3.0)
    }

}