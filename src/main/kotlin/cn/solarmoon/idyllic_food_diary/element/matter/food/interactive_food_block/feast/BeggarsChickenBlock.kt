package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.IInteraction
import net.minecraft.core.BlockPos
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class BeggarsChickenBlock: FoodEntityBlock(Properties.of()) {

    override val maxInteraction: Int
        get() = 5

    override fun getFoodShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return Shapes.block()
    }

    override fun getInteraction(stage: Int): IInteraction {
        return ConsumeInteraction(5, FoodProperties.Builder().nutrition(1).build())
    }

}