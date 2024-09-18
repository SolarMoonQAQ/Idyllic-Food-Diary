package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.recipe.EvaporationRecipe
import cn.solarmoon.idyllic_food_diary.element.recipe.SteamingRecipe
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.idyllic_food_diary.registry.common.IFDRecipes
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.recipe.processor.RecipeProcessorHelper
import cn.solarmoon.spark_core.api.util.DropUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

open class SteamerBlockEntity(pos: BlockPos, state: BlockState) : SyncedBlockEntity(IFDBlockEntities.STEAMER.get(), pos, state) {

    val maxLayer = 4
    val inventories = mutableListOf<SteamerInventory>()
    val invHandler = SteamerItemHandler(inventories)
    var lid = ItemStack.EMPTY
    val hasLid
        get() = !lid.isEmpty
    val steaming = SteamingRecipe.Processor(this, invHandler)
    val baseInv // 最基本的可添加物品栏
        get() = SteamerInventory(4).applyTile(this)
    val presentLayer // 当前层数
        get() = inventories.size

    init {
        // 无论如何新蒸笼一定有且仅有一个物品栏
        if (inventories.isEmpty()) inventories.add(baseInv)
    }

    /**
     * 将stack里的所有层一次性放入该蒸笼
     *
     * 只对方块内容进行了操作，对输入物无任何操作，需自行补齐
     */
    fun addLayerFromItem(stack: ItemStack, registries: HolderLookup.Provider): Boolean {
        val level = level ?: return false
        if (!stack.`is`(IFDItems.STEAMER)) return false // 不是蒸笼直接滚蛋
        val listTag = SteamerItem.getInvListTag(stack, registries)
        val stackLayer = listTag.size
        if (stackLayer > maxLayer - presentLayer) return false // 必须保证所要添加的层数加完后不超过设定的最大层数
        listTag.forEach {
            val inv = baseInv
            inv.deserializeNBT(registries, it as CompoundTag)
            inventories.add(inv)
        }
        // 处理盖子逻辑：方块没盖子，就从物品中拿，无论物品里有没有。都有盖子就从方块里（方块盖子）弹一个出来
        val itemLid = SteamerItem.getLid(stack, registries)
        if (!hasLid) {
            lid = itemLid
            SteamerItem.setLid(stack, ItemStack.EMPTY, registries)
        } else if (!itemLid.isEmpty) {
            DropUtil.summonDrop(lid, level, blockPos.bottomCenter.add(0.0, presentLayer / maxLayer.toDouble(), 0.0))
        }
        setChanged()
        return true
    }

    /**
     * 将当前最后一层数据移到物品中
     */
    fun takeLastLayerToItem(stack: ItemStack, player: Player, registries: HolderLookup.Provider): Boolean {
        val level = level ?: return false
        var doTrans = false
        var lidControlStack = ItemStack.EMPTY
        // 空手，直接拿一个出来给上全新一层（仅一层）
        if (stack.isEmpty) {
            val take = inventories.last()
            val result = ItemStack(IFDItems.STEAMER)
            result.update(DataComponents.BLOCK_ENTITY_DATA, SteamerItem.singleEmptyLayer(registries)) {
                it.update { t ->
                    val listTag = ListTag() // 一定是创建一个新的，因为蒸笼本身自带一层空层，否则就是加两层了
                    listTag.add(take.serializeNBT(registries))
                    t.put(INV_LIST, listTag)
                }
            }
            player.setItemInHand(InteractionHand.MAIN_HAND, result)
            lidControlStack = result
            doTrans = true
        // 否则就只能是蒸笼
        } else if (stack.`is`(IFDItems.STEAMER)) {
            val listTag = SteamerItem.getInvListTag(stack, registries)
            val itemLayer = listTag.size
            // 保证蒸笼不会超出最大层数
            if (itemLayer < maxLayer) {
                listTag.add(inventories.last().serializeNBT(registries))
                stack.update(DataComponents.BLOCK_ENTITY_DATA, SteamerItem.singleEmptyLayer(registries)) {
                    it.update { it.put(INV_LIST, listTag) }
                }
                lidControlStack = stack
                doTrans = true
            }
        }

        // 执行对蒸笼的削减或删除
        if (doTrans) {
            // 只有一层，直接把方块删了
            if (presentLayer <= 1) level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3)
            else {
                inventories.removeLast()
                setChanged()
            }
            // 处理盖子逻辑：物品没盖子，就从方块中拿，无论方块里有没有，都有盖子就从玩家嘴里（方块盖子）弹一个出来
            val itemLid = SteamerItem.getLid(lidControlStack, registries)
            if (itemLid.isEmpty) {
                SteamerItem.setLid(lidControlStack, lid, registries)
                clearLid()
            } else if (hasLid) {
                player.drop(itemLid, false)
                clearLid()
            }
            return true
        }

        return false
    }

    /**
     * @return 获取连接底部的基座
     */
    fun getBase(): EvaporationRecipe.Processor? {
        level?.let {
            val blockEntity = it.getBlockEntity(connectedBelowPos) ?: return null
            val eva = RecipeProcessorHelper.getMap(blockEntity)[IFDRecipes.EVAPORATION]
            if (eva != null) {
                return eva as EvaporationRecipe.Processor?
            }
        }
        return null
    }

    /**
     * @return 获取连接顶部的蒸笼
     */
    fun getTop(): SteamerBlockEntity? {
        level?.let {
            val blockEntity = it.getBlockEntity(connectedTopPos)
            if (blockEntity is SteamerBlockEntity) {
                return blockEntity
            }
        }
        return null
    }

    /**
     * 获取连接的蒸笼的最下方的第一个非蒸笼（或一层蒸笼）方块坐标
     *
     * 要求是上方为蒸笼且stack为最大堆叠量才继续检测
     *
     * 也就是直到检测到非蒸笼或stack不满的蒸笼为止
     */
    val connectedBelowPos: BlockPos
        get() {
            level?.let {
                var posBelow = blockPos.below() // 不用检测自己直接从下一格开始
                var be = it.getBlockEntity(posBelow)
                // 当前坐标是蒸笼，并且栈最大，检测坐标就下移一格
                while (be is SteamerBlockEntity && be.presentLayer == maxLayer) {
                    posBelow = posBelow.below()
                    be = it.getBlockEntity(posBelow) // 重设防止无限循环
                }
                // 当前坐标不是蒸笼，或者蒸笼层数不满就返回
                return posBelow
            } ?: run { return BlockPos.ZERO }
        }

    /**
     * 获取连接的蒸笼的最上方的第一个非蒸笼（或一层蒸笼）方块坐标
     *
     * 要求是上方为蒸笼且stack为最大堆叠量才继续检测
     *
     * 也就是直到检测到非蒸笼或stack不满的蒸笼为止
     */
    val connectedTopPos: BlockPos
        get() {
            level?.let {
                var posAbove = blockPos
                var be = it.getBlockEntity(posAbove)
                // 当前坐标是蒸笼，并且栈最大，检测坐标就上移一格
                while (be is SteamerBlockEntity && be.presentLayer == maxLayer) {
                    posAbove = posAbove.above()
                    be = it.getBlockEntity(posAbove) // 重设防止无限循环
                }
                // 循环结束，如果当前坐标不是蒸笼，那么实际坐标就在之前一格
                // 如果是，就返回当前坐标
                return if (be is SteamerBlockEntity) posAbove else posAbove.below()
            } ?: run {
                return BlockPos.ZERO
            }
        }

    val isTop
        get() = blockPos == connectedTopPos

    val isBottom: Boolean
        get() = blockPos.below() == connectedBelowPos

    val isMiddle: Boolean
        get() = !isTop && !isBottom

    /**
     * 检测是否在连接状态（下方是否有stack为最大的蒸笼）
     *
     * 逻辑是找连接底端坐标，如果坐标和当前坐标的下方一致，那就说明下方没有蒸笼，上方同理
     */
    val isConnected: Boolean
        get() = (connectedBelowPos != blockPos.below()
                || connectedTopPos != blockPos)

    /**
     * 不让在连接中段放盖子
     */
    fun tryPreventLidSet() {
        val level = level ?: return
        val pos = blockPos
        if (isConnected && hasLid && !isTop) {
            DropUtil.summonDrop(lid.copy(), level, pos.center)
            clearLid()
        }
    }

    fun clearLid() {
        lid = ItemStack.EMPTY
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        val listTag = ListTag()
        inventories.forEach { listTag.add(it.serializeNBT(registries)) }
        tag.put(INV_LIST, listTag)
        tag.put(LID, lid.saveOptional(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        // 此处逻辑是这样：因为默认情况下物品组初始化是一个物品其它都是空位，因此如果tag有物品栏可以读的话，先清空所有槽位再加上解析后的新槽就完美读取了
        val listTag = tag.getList(INV_LIST, ListTag.TAG_COMPOUND.toInt())
        if (listTag.isNotEmpty()) {
            inventories.clear()
            listTag.forEach { tag ->
                val inv = baseInv
                inv.deserializeNBT(registries, tag as CompoundTag)
                inventories.add(inv)
            }
        }
        lid = ItemStack.parseOptional(registries, tag.getCompound(LID))
    }

    companion object {
        const val INV_LIST = "InvList"
        const val LID = "Lid"
    }

}