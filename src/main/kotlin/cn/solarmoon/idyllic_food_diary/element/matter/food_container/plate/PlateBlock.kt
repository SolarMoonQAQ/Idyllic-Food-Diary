package cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate

import cn.solarmoon.idyllic_food_diary.element.matter.food_container.FoodContainerBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class PlateBlock(soundType: SoundType): FoodContainerBlock(soundType) {

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return Shapes.or(box(5.0, 0.0, 5.0, 13.0, 1.0, 13.0), box(1.0, 1.0, 1.0, 15.0, 2.0, 15.0))
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.PLATE.get()
    }

}