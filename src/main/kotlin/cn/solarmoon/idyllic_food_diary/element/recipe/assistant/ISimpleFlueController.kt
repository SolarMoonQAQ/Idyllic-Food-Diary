package cn.solarmoon.idyllic_food_diary.element.recipe.assistant

import cn.solarmoon.spark_core.api.blockstate.ILitState
import cn.solarmoon.spark_core.api.recipe.processor.IProcessorAssistant
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.FlintAndSteelItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

/**
 * Lit的控制器，用于协助处理需要简单燃料的配方
 */
interface ISimpleFlueController: IProcessorAssistant {

    var burnTime: Int
    var flueTime: Int
    val flue: ItemStack // 改get方法

    fun getBlockEntity(): BlockEntity

    fun tryControlLit() {
        val state = getBlockEntity().blockState
        val pos = getBlockEntity().blockPos
        val level = getBlockEntity().level ?: return
        //消耗煤炭，控制lit属性
        if (state.getValue(ILitState.LIT)) {
            //有燃料就保存其燃烧时间，并且消耗一个
            if (!noFuel() && flueTime == 0) {
                flueTime = flue.getBurnTime(null)
                flue.shrink(1)
                getBlockEntity().setChanged()
            }
            burnTime++
            //燃烧时间超过燃料所能提供，就刷新
            if (burnTime >= flueTime) {
                burnTime = 0
                flueTime = 0
            }
            //无燃料且没有燃烧时间了就停止lit
            if (noFuel() && burnTime == 0) {
                level.setBlock(pos, state.setValue(ILitState.LIT, false), 3)
                getBlockEntity().setChanged()
            }
        }
    }

    /**
     * 手动点燃燃料
     * @return 成功返回true
     */
    fun litByHand(player: Player, hand: InteractionHand): Boolean {
        val heldItem = player.getItemInHand(hand)
        val pos = getBlockEntity().blockPos
        val state = getBlockEntity().blockState
        val level = getBlockEntity().level ?: return false
        //打火石等点燃
        if (!state.getValue(ILitState.LIT) && !flue.isEmpty) {
            if (heldItem.item is FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, player.random.nextFloat() * 0.4f + 0.8f)
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, java.lang.Boolean.TRUE), 11)
                heldItem.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))
                return true
            }
        }
        return false
    }

    fun isBurning(): Boolean {
        val state: BlockState = getBlockEntity().blockState
        return state.getValue(ILitState.LIT)
    }

    fun noFuel(): Boolean {
        return flue.isEmpty
    }

    override fun aSave(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.putInt("BurnTime", burnTime)
        tag.putInt("FlueTime", flueTime)
    }

    override fun aLoad(tag: CompoundTag, registries: HolderLookup.Provider) {
        burnTime = tag.getInt("BurnTime")
        flueTime = tag.getInt("FlueTime")
    }

}