package cn.solarmoon.idyllic_food_diary.element.matter.food_container.long_plate

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.LongFoodContainerBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BedPart
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class LongPlateBlock(soundType: SoundType): LongFoodContainerBlock(soundType) {
    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        val combine1 = Shapes.or(box(1.0, 1.0, 1.0, 15.0, 2.0, 16.0), box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0))
        val combine2 = Shapes.or(box(1.0, 1.0, 0.0, 15.0, 2.0, 15.0), box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0))
        return if (state.getValue(IBedPartState.PART) == BedPart.HEAD) VoxelShapeUtil.rotateShape(state.getValue(IHorizontalFacingState.FACING), combine1)
        else VoxelShapeUtil.rotateShape(state.getValue(IHorizontalFacingState.FACING), combine2)
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.LONG_PLATE.get()
    }
}