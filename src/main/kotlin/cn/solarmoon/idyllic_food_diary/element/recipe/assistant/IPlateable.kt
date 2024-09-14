package cn.solarmoon.idyllic_food_diary.element.recipe.assistant

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * 需要预存输出内容的可接入
 */
interface IPlateable: IExpGiver {

    var result: ItemStack
    var container: Ingredient

    /**
     * 将预存的结果给予玩家
     *
     * 并且会将玩家手中盛放的容器信息存入食物
     */
    fun tryGiveResult(player: Player, hand: InteractionHand, mainHandOnly: Boolean): Boolean {
        if (mainHandOnly && hand != InteractionHand.MAIN_HAND) return false
        val heldItem = player.getItemInHand(hand)
        return tryGiveResult(player, heldItem)
    }

    fun getBlockEntity(): BlockEntity

    /**
     * 将预存的结果给予玩家
     *
     * 并且会将玩家手中盛放的容器信息存入食物
     */
    fun tryGiveResult(player: Player, heldItem: ItemStack): Boolean {
        if (hasResult()) {
            if (container.test(heldItem)) {
                val result = result.split(1)
                if (!heldItem.isEmpty) result.set(IFDDataComponents.FOOD_CONTAINER, FoodContainer(heldItem))
                if (!player.isCreative) heldItem.shrink(1)
                DropUtil.addItemToInventory(player, result)
                giveExp(player, true)
                resetContainer()
                getBlockEntity().setChanged()
                return true
            } else {
                var message = IdyllicFoodDiary.TRANSLATOR.set("message", "container_required.empty")
                if (!container.isEmpty) {
                    message = IdyllicFoodDiary.TRANSLATOR.set("message", "container_required")
                }
                player.displayClientMessage(message, true)
            }
        }
        return false
    }

    fun resetContainer() {
        container = Ingredient.EMPTY
    }

    /**
     * @return 预留槽是否被占用
     */
    fun hasResult(): Boolean {
        return !result.isEmpty
    }

    /**
     * 同时设置输出结果和匹配容器
     */
    fun setPending(result: ItemStack, container: Ingredient) {
        this.result = result
        this.container = container
    }

    override fun aSave(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.aSave(tag, registries)
        tag.put("Result", result.saveOptional(registries))
        tag.put("Container", Ingredient.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), container).orThrow)
    }

    override fun aLoad(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.aLoad(tag, registries)
        result = ItemStack.parseOptional(registries, tag.getCompound("Result"))
        container = Ingredient.CODEC.decode(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("Container")).orThrow.first
    }

}