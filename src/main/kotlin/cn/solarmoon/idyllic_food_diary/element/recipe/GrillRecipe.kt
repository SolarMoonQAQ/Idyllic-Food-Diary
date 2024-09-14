package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.recipe.assistant.ISimpleFlueController
import cn.solarmoon.spark_core.api.blockstate.ILitState
import cn.solarmoon.spark_core.api.recipe.processor.MultiTimeRecipeProcessor
import net.minecraft.world.item.crafting.CampfireCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.items.ItemStackHandler

class GrillRecipe {

    class Processor(be: BlockEntity, val inv: ItemStackHandler): MultiTimeRecipeProcessor<SingleRecipeInput, CampfireCookingRecipe>(be), ISimpleFlueController {

        override var burnTime = 0
        override var flueTime = 0
        override val flue get() = inv.getStackInSlot(6)

        override fun isRecipeMatch(recipe: CampfireCookingRecipe, index: Int): Boolean {
            val stack = inv.getStackInSlot(index)
            return recipe.ingredients[0].test(stack) && be.blockState.getValue(ILitState.LIT)
        }

        override fun tryWork(): Boolean {
            tryControlLit() // 控制行星发动机点火
            val level = be.level ?: return false
            if (level.isClientSide) return false
            var flag = false
            for (i in 0 until inv.slots) {
                findRecipe(i)?.let {
                    times[i]++
                    recipeTimes[i] = it.value.cookingTime / 3
                    if (times[i] > recipeTimes[i]) {
                        val out = it.value.getResultItem(level.registryAccess())
                        inv.setStackInSlot(i, out)
                        times[i] = 0
                        recipeTimes[i] = 0
                        be.setChanged()
                    }
                    flag = true
                } ?: let {
                    times[i] = 0
                    recipeTimes[i] = 0
                }
            }
            return flag
        }

        override fun getRecipeType(): RecipeType<CampfireCookingRecipe> = RecipeType.CAMPFIRE_COOKING

        override fun getBlockEntity(): BlockEntity = be

    }

}