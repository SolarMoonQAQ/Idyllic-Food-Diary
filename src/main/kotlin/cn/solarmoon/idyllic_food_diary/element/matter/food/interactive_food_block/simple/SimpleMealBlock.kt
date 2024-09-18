package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.simple

import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.IInteraction
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

/**
 * 此类方块特点：
 *
 * 只有一次交互且为吃，放下以后没有容器垫底，物品形式的该方块也是可食用的
 */
abstract class SimpleMealBlock: FoodEntityBlock(Properties.of().sound(SoundType.WOOL).strength(1f)) {

    override val maxInteraction: Int = 1

    override fun getInteraction(stage: Int): IInteraction {
        return ConsumeInteraction(5, ItemStack(this).getFoodProperties(null)!!)
    }

}