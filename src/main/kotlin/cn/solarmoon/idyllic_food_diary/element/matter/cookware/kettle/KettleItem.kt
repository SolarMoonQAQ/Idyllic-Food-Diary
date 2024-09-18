package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.tooltip.TooltipOperator
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidUtil

class KettleItem(block: Block, properties: Properties): BlockItem(block, properties.component(IFDDataComponents.FLUID_INTERACT_VALUE, 1000)) {

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack = player.getItemInHand(usedHand)
        val hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY)
        val pos = hit.blockPos
        val side = hit.direction
        val result = FluidUtil.tryPickUpFluid(stack, player, level, pos, side)
        if (result.isSuccess) {
            return InteractionResultHolder.sidedSuccess(result.result, level.isClientSide)
        }
        return super.use(level, player, usedHand)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player
        if (player?.isCrouching == true) return super.useOn(context)
        return InteractionResult.PASS
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
        val dump = stack.get(IFDDataComponents.FLUID_INTERACT_VALUE)!!
        TooltipOperator(tooltipComponents).addShiftShowTooltip {
            tooltipComponents.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "kettle.dumping_volume", dump))
        }
    }

}