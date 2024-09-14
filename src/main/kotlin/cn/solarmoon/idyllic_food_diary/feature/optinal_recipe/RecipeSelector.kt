package cn.solarmoon.idyllic_food_diary.feature.optinal_recipe

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinItem
import cn.solarmoon.idyllic_food_diary.netwrok.ServerNetHandler
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.network.CommonNetData
import cn.solarmoon.spark_core.api.util.HitResultUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.network.PacketDistributor

/**
 * 配方选择器，用于控制可选配方的gui和物品选择序列，需要在客户端事件中调用注册方法
 */
abstract class RecipeSelector(protected val gui: OptionalRecipeGui) {

    fun scrollMouse(event: InputEvent.MouseScrollingEvent) {
        Minecraft.getInstance().player?.let { player ->
            val heldItem = player.mainHandItem
            val selectData = heldItem.getOrDefault(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY)
            if (player.isCrouching) {
                val hitBlock = HitResultUtil.getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.NONE)
                val level = player.level()
                if (condition(player, player.level(), selectData, heldItem, hitBlock) && gui.shouldRender()) {
                    val input = level.getBlockState(hitBlock.blockPos).block
                    val size = gui.getItemsShown(input, level).size
                    val presentIndex = selectData.getIndex(input)
                    var target: Int
                    if (event.scrollDeltaY > 0) {
                        target = (presentIndex + 1 + size) % size
                        gui.up()
                        level.playLocalSound(player, IFDSounds.WOODEN_FISH.get(), SoundSource.PLAYERS, 0.5f, 1f)
                    } else {
                        target = (presentIndex - 1 + size) % size
                        gui.down()
                        level.playLocalSound(player, IFDSounds.WOODEN_FISH.get(), SoundSource.PLAYERS, 0.5f, 1f)
                    }
                    heldItem.update(IFDDataComponents.RECIPE_SELECTION, RecipeSelectData.EMPTY) { it.putIndexAndCopy(input, target) }
                    PacketDistributor.sendToServer(CommonNetData(block = input, intValue = target, message = ServerNetHandler.RECIPE_SELECT))
                    event.isCanceled = true
                }
            }
        }
    }

    /**
     * 默认方法中，必须手持物品匹配且玩家为蹲下才能通过，这里则需要填入对具体方块的通过条件，否则拿着有选择器的物品使用滚轮会随时生效
     */
    abstract fun condition(player: LocalPlayer, level: Level, selectData: RecipeSelectData, mainItem: ItemStack, hitResult: BlockHitResult): Boolean

    fun register() {
        NeoForge.EVENT_BUS.addListener(::scrollMouse)
    }

}