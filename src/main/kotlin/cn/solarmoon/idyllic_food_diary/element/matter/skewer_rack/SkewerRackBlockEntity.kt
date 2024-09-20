package cn.solarmoon.idyllic_food_diary.element.matter.skewer_rack

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.spark_core.api.attachment.animation.Timer
import cn.solarmoon.spark_core.api.blockentity.SyncedBlockEntity
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.cap.item.TileInventory
import cn.solarmoon.spark_core.api.util.DropUtil
import cn.solarmoon.spark_core.registry.common.SparkAttachments
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class SkewerRackBlockEntity(pos: BlockPos, state: BlockState): SyncedBlockEntity(IFDBlockEntities.SKEWER_RACK.get(), pos, state) {

    val inventory = object : TileInventory(this, 1, 1) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return !isConnectedWithFence
        }
    }

    companion object {
        const val ANIM_ROTATION = "rotation"
    }

    init {
        val anim = getData(SparkAttachments.ANIMTICKER)
        anim.timers[ANIM_ROTATION] = Timer().apply { maxTime = 30f }
    }

    /**
     * 上下方是否有栅栏连接
     */
    val isConnectedWithFence: Boolean
        get() {
            val level = level ?: return false
            val belowBlock = level.getBlockState(blockPos.below())
            val aboveBlock = level.getBlockState(blockPos.above())
            return belowBlock.`is`(BlockTags.FENCES) || aboveBlock.`is`(BlockTags.FENCES)
        }

    /**
     * 是否成结构
     *
     * 以任意一个烤架为中心点：
     * 当前方向的两边侧方，都必须有相似方向（必须视觉上连接）的烤架相连，且连接的终端被上或下方栅栏相连
     *
     */
    val structure: SkewerRackStructure?
        get() {
            val level = level ?: return null
            val direction = blockState.getValue(IHorizontalFacingState.FACING)
            val directionL = direction.counterClockWise; var posL = blockPos.relative(directionL); var blockL = level.getBlockState(posL)
            val directionR = direction.clockWise; var posR = blockPos.relative(directionR); var blockR = level.getBlockState(posR)
            val all = mutableListOf<SkewerRackBlockEntity>()
            all.add(this)
            while (blockL.`is`(blockState.block) && blockL.getValue(IHorizontalFacingState.FACING) in listOf(direction, direction.opposite)) {
                all.add(level.getBlockEntity(posL) as SkewerRackBlockEntity)
                posL = posL.relative(directionL)
                blockL = level.getBlockState(posL)
            }
            while (blockR.`is`(blockState.block) && blockR.getValue(IHorizontalFacingState.FACING) in listOf(direction, direction.opposite)) {
                all.add(level.getBlockEntity(posR) as SkewerRackBlockEntity)
                posR = posR.relative(directionR)
                blockR = level.getBlockState(posR)
            }
            val rackL = level.getBlockEntity(posL.relative(directionR)).takeIf { it is SkewerRackBlockEntity } ?: return null
            val rackR = level.getBlockEntity(posR.relative(directionL)).takeIf { it is SkewerRackBlockEntity } ?: return null
            return SkewerRackStructure(rackL as SkewerRackBlockEntity, rackR as SkewerRackBlockEntity, all).takeIf { rackL.isConnectedWithFence && rackR.isConnectedWithFence }
        }

    /**
     * 获取连接的栅栏
     * @param belowOrAbove UP定位上方连接的栅栏， DOWN定位下方的，别的方向也行但没意义
     */
    fun getConnectedFence(belowOrAbove: Direction): BlockState? {
        val level = level ?: return null
        if (isConnectedWithFence) {
            val fence = level.getBlockState(blockPos.relative(belowOrAbove))
            return fence.takeIf { it.`is`(BlockTags.FENCES) }
        }
        return null
    }

    /**
     * @return 快速获取上或下的任意一个连接的栅栏，优先获取下面的栅栏
     */
    fun getConnectedValidFence(): BlockState? {
        return listOf(getConnectedFence(Direction.DOWN), getConnectedFence(Direction.UP)).firstOrNull { it != null }
    }

    /**
     * 将内部物品全部掉出，相当于阻止物品放入
     */
    fun preventItemIn() {
        val level = level ?: return
        DropUtil.summonDrop(ItemStackHandlerHelper.getStacks(inventory).map { it.copy() }, level, blockPos.center)
        ItemStackHandlerHelper.clearInv(inventory)
        setChanged()
    }

    override fun setRemoved() {
        preventItemIn()
        super.setRemoved()
    }

    fun syncRot() {
        // 多方块结构成立就同步旋转角，否则重置旋转角
        structure?.syncRot() ?: run {
            val anim = getData(SparkAttachments.ANIMTICKER)
            anim.timers[ANIM_ROTATION]!!.stop()
            anim.fixedValues["rot"] = 0f
            anim.fixedValues["velocity"] = 0f
        }
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ItemStackHandlerHelper.ITEM, inventory.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        inventory.deserializeNBT(registries, tag.getCompound(ItemStackHandlerHelper.ITEM))
    }

}