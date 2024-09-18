package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.fluid_effect.FluidEffect
import cn.solarmoon.idyllic_food_diary.feature.util.DrinkUtil
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.Capabilities

class CupItem(block: Block, properties: Properties): BlockItem(block, properties) {

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int {
        val tank = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return 0
        val fluid = tank.getFluidInTank(0).fluid
        val foodEffect = FluidEffect.get(fluid)?.foodProperties ?: return 0
        return foodEffect.eatDurationTicks()
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.DRINK
    }

    /**
     * 只能蹲下放置
     */
    override fun useOn(context: UseOnContext): InteractionResult {
        if (context.player?.isCrouching == false) return InteractionResult.PASS
        return super.useOn(context)
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val heldItem = player.getItemInHand(usedHand)
        val tank = heldItem.getCapability(Capabilities.FluidHandler.ITEM) ?: return InteractionResultHolder.fail(heldItem)
        if (DrinkUtil.canDrink(player, tank.getFluidInTank(0).fluid)) {
            player.startUsingItem(usedHand)
            return InteractionResultHolder.consume(heldItem)
        }
        return super.use(level, player, usedHand)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        DrinkUtil.addTooltip(stack, tooltipComponents)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        DrinkUtil.itemDrink(stack, livingEntity)
        return super.finishUsingItem(stack, level, livingEntity)
    }

}