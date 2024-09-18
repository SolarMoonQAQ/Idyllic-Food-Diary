package cn.solarmoon.idyllic_food_diary.feature.util

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.fluid_effect.FluidEffect
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.max

object DrinkUtil {

    /**
     * 应用液体效果，当可喝的液体量不足所需消耗量时不触发任何效果，只是消耗液体
     */
    @JvmStatic
    fun drink(entity: LivingEntity, fluidStack: FluidStack): Boolean {
        val level = entity.level()
        val effect = FluidEffect.get(fluidStack.fluid) ?: return false
        if (fluidStack.amount < effect.consumeAmount) {
            fluidStack.shrink(effect.consumeAmount)
            return false
        }
        if (effect.clearAllEffects) entity.removeAllEffects()
        entity.eat(level, ItemStack.EMPTY, effect.foodProperties)
        entity.remainingFireTicks = max(entity.remainingFireTicks, effect.fire)
        if (effect.extinguishing) entity.extinguishFire()
        fluidStack.shrink(effect.consumeAmount)
        return true
    }

    /**
     * 由于上面那个方法直接削减的fluidStack，对于item这类固定保存信息的不起作用，可用这个简化对item的液体修改
     */
    @JvmStatic
    fun itemDrink(stack: ItemStack, entity: LivingEntity): Boolean {
        val tank = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return false
        val fluidStack = tank.getFluidInTank(0)
        val effect = FluidEffect.get(fluidStack.fluid) ?: return false
        if (drink(entity, fluidStack)) {
            tank.drain(effect.consumeAmount, IFluidHandler.FluidAction.EXECUTE)
            return true
        }
        return false
    }

    @JvmStatic
    fun canDrink(player: Player, fluid: Fluid): Boolean {
        val effect = FluidEffect.get(fluid) ?: return false
        return player.canEat(effect.foodProperties.canAlwaysEat)
    }

    @JvmStatic
    fun addTooltip(stack: ItemStack, tooltipComponents: MutableList<Component?>) {
        val tank = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return
        val effect = FluidEffect.get(tank.getFluidInTank(0).fluid) ?: return
        val effects = effect.foodProperties.effects.map { it.effect() }
        val to = TooltipOperator(tooltipComponents)
        to.addPotionLikeTooltip(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_effect.fire"), effect.fire)
        to.addPotionTooltipWithoutAttribute(effects)
    }

}