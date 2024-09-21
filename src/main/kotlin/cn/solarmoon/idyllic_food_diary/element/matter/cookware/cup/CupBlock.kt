package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.feature.util.DrinkUtil
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.blockentity.SyncedEntityBlock
import cn.solarmoon.spark_core.api.cap.fluid.FluidHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidUtil

abstract class CupBlock(properties: Properties): CookwareBlock(properties) {

    override fun useItemOnThis(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val cup = level.getBlockEntity(pos) as CupBlockEntity

        if (!level.isClientSide && FluidHandlerHelper.putFluid(cup.tank, player, hand, false)) {
            level.playSound(null, pos, IFDSounds.POURING_WATER.get(), SoundSource.PLAYERS)
            return ItemInteractionResult.SUCCESS
        } else if (!level.isClientSide && FluidHandlerHelper.takeFluid(cup.tank, player, hand, true)) {
            return ItemInteractionResult.SUCCESS
        }

        if (ItemStackHandlerHelper.storage(cup.inventory, player, hand, 1, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER.value(), SoundSource.PLAYERS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }

        if (DrinkUtil.canDrink(player, cup.tank.fluid.fluid)) {
            val counter = player.getData(SparkAttachments.COUNTING_DEVICE)
            level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS)
            counter.setCount(counter.count + 1, pos)
            if (counter.count > 2) {
                DrinkUtil.drink(player, cup.tank.fluid)
                cup.tank.onContentsChanged()
                counter.setCount(0)
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return ItemInteractionResult.CONSUME
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        val cup = blockEntity as CupBlockEntity
        cup.brew.tryWork()
    }

}