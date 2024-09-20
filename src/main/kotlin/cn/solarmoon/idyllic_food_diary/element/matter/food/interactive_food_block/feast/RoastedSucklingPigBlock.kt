package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodPropertyData
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.IInteraction
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class RoastedSucklingPigBlock: FoodEntityBlock(Properties.of().sound(SoundType.WOOL)), IBedPartState {

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
        return ConsumeInteraction(5, FoodPropertyData.PRIMARY_1)
    }

}