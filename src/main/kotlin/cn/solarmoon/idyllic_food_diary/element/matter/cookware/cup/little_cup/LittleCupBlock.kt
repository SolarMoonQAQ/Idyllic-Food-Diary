package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CupBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.stream.Stream

class LittleCupBlock(properties: Properties): CupBlock(properties) {

    override fun getShapeThis(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return Stream.of(
            box(6.0, 1.0, 7.0, 7.0, 5.0, 10.0),
            box(9.0, 1.0, 6.0, 10.0, 5.0, 9.0),
            box(6.0, 1.0, 6.0, 9.0, 5.0, 7.0),
            box(7.0, 1.0, 9.0, 10.0, 5.0, 10.0),
            box(6.0, 0.0, 6.0, 10.0, 1.0, 10.0)
        ).reduce { v1, v2 -> Shapes.or(v1, v2) }.get()
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.LITTLE_CUP.get()
    }

}