package cn.solarmoon.idyllic_food_diary.element.recipe

import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp
import cn.solarmoon.idyllic_food_diary.feature.util.HeatSourceUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.recipe.processor.SingleTimeRecipeProcessor
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.FluidTank
import net.neoforged.neoforge.items.wrapper.RecipeWrapper

class EvaporationRecipe {

    class Processor(be: BlockEntity, val tank: FluidTank): SingleTimeRecipeProcessor<RecipeWrapper, Recipe<RecipeWrapper>>(be) {

        override fun tryWork(): Boolean {
            if (canEvaporate()) {
                time++
                recipeTime = getEvaporatingTime()
                if (time >= recipeTime) {
                    time = 0
                    tank.drain(getEvaporatingAmount(), IFluidHandler.FluidAction.EXECUTE)
                    be.setChanged()
                }
                return true
            } else {
                time = 0
                return false
            }
        }

        override fun isRecipeMatch(recipe: Recipe<RecipeWrapper>): Boolean {
            return canEvaporate()
        }

        fun canEvaporate(): Boolean {
            val level = be.level
            val pos = be.blockPos
            val state = be.blockState
            val block = state.block
            if (block is IBuiltInStove) {
                // 当是炉灶嵌入模式且上方有盖子，就不蒸水
                if (block.isNestedInStove(state)) {
                    if (level != null && level.getBlockState(pos.above()).`is`(IFDBlocks.STOVE_LID.get())) {
                        return false
                    }
                    // 当可以镶嵌入炉灶时，只能镶嵌中才能蒸水
                    return HeatSourceUtil.isOnHeatSource(be) && hasHotFluid()
                }
                return false
            }
            return HeatSourceUtil.isOnHeatSource(be) && hasHotFluid()
        }

        override fun isWorking(): Boolean = canEvaporate()

        /**
         * 默认每tick蒸发的液体量
         */
        fun getEvaporatingAmount(): Int = 5

        /**
         * 每多少tick蒸发一次液体
         */
        fun getEvaporatingTime(): Int = 20

        /**
         * @return 是否可供给蒸笼工作
         */
        fun isValidForSteamer(): Boolean {
            return HeatSourceUtil.isOnHeatSource(be) && hasHotWater()
        }

        /**
         * 是否存在热水（就特指水）
         */
        fun hasHotWater(): Boolean {
            return hasHotFluid() && tank.fluid.fluid === Fluids.WATER
        }

        /**
         * 是否存在热的液体
         */
        fun hasHotFluid(): Boolean {
            return tank.fluid.getOrDefault(IFDDataComponents.TEMP, Temp.COMMON) == Temp.HOT
        }

        override fun getRecipeType(): RecipeType<Recipe<RecipeWrapper>> = IFDRecipes.EVAPORATION

    }

}