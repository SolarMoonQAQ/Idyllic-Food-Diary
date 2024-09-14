package cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.ILitState
import cn.solarmoon.spark_core.api.renderer.IFreeRenderBlock
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import javax.swing.plaf.nimbus.State

interface IBuiltInStove: IFreeRenderBlock, ILitState {

    companion object {
        val NESTED_IN_STOVE = BooleanProperty.create("nested_in_stove")
    }

    /**
     * 修改以后不要用这个来看偏移量，用下面的get方法
     */
    val yOffset: Double
        get() = 8.0 / 16.0

    fun getShape(state: BlockState): VoxelShape {
        val direction = state.getValue(IHorizontalFacingState.FACING)
        val i = Shapes.block()
        val ii = Block.box(5.0, 2.0, 0.0, 11.0, 7.0, 12.0)
        val iii = Block.box(2.0, 10.0, 2.0, 14.0, 16.0, 14.0)
        val result = Shapes.joinUnoptimized(i, Shapes.or(ii, iii), BooleanOp.ONLY_FIRST)
        return VoxelShapeUtil.rotateShape(direction, result)
    }

    /**
     * 用于renderer，将方块上移至炉灶槽位的高度
     */
    fun translateContent(direction: Direction, poseStack: PoseStack, buffer: MultiBufferSource, light: Int, overlay: Int) {
        Minecraft.getInstance().blockRenderer.renderSingleBlock(IFDBlocks.INLAID_STOVE.get().defaultBlockState().setValue(IHorizontalFacingState.FACING, direction), poseStack, buffer, light, overlay)
        poseStack.translate(0.01/16, yOffset, 0.01/16)
        poseStack.scale((1 - 0.02/16).toFloat(), (1 - 0.02/16).toFloat(), (1 - 0.02/16).toFloat())
    }

    fun getYOffset(state: BlockState): Double {
        val block = state.block
        return if (block is IBuiltInStove && block.isNestedInStove(state)) yOffset else 0.0
    }

    fun isNestedInStove(state: BlockState): Boolean {
        return state.getValue(NESTED_IN_STOVE)
    }

}