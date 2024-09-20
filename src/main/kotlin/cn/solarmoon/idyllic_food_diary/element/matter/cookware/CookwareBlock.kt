package cn.solarmoon.idyllic_food_diary.element.matter.cookware

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.InlaidStoveBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.idyllic_food_diary.registry.common.IFDItems
import cn.solarmoon.spark_core.api.blockentity.SyncedEntityBlock
import cn.solarmoon.spark_core.api.blockstate.IBedPartState
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.ILitState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.registry.common.SparkDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.SimpleFluidContent
import test.be

abstract class CookwareBlock(properties: Properties): SyncedEntityBlock(properties.lightLevel(ILitState::getCommonLightLevel)), IHorizontalFacingState {

    init {
        if (this is IBuiltInStove) {
            this.registerDefaultState(this.getStateDefinition().any().setValue(IBuiltInStove.NESTED_IN_STOVE, false));
        }
    }

    abstract fun useItemOnThis(heldItem: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult

    abstract fun getShapeThis(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape

    override fun useItemOn(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val block = state.block
        // 当炉灶镶嵌模式时，增加点火功能，并且把使用范围限制在镶嵌槽内
        if (block is IBuiltInStove && block.isNestedInStove(state)) {
            // 锅盖啥的直接放
            if (heldItem.`is`(IFDItems.STOVE_LID)) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION

            if (InlaidStoveBlock.isClickInStove(hitResult, pos)) {
                return useItemOnThis(heldItem, state, level, pos, player, hand, hitResult)
            }

            if (ILitState.controlLitByHand(state, pos, level, player, hand)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide)
            }

            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }
        return useItemOnThis(heldItem, state, level, pos, player, hand, hitResult)
    }

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player
    ): ItemStack {
        return if (dropSync()) super.getCloneItemStack(state, target, level, pos, player) else ItemStack(this)
    }

    override fun getDrops(state: BlockState, builder: LootParams.Builder): List<ItemStack> {
        val drops = if (dropSync()) super.getDrops(state, builder).toMutableList() else mutableListOf()
        // 不保存容器信息时，掉落容器内物品
        if (!dropSync()) {
            val be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            be?.level?.let { level ->
                level.getCapability(Capabilities.ItemHandler.BLOCK, be.blockPos, Direction.NORTH)?.let { inv ->
                    drops.addAll(ItemStackHandlerHelper.getStacks(inv))
                    drops.add(ItemStack(this))
                }
            }
        }
        // 如果可镶嵌在灶台内，不保存stove镶嵌信息，打破带炉灶的厨具后自动拆分
        drops.stream().filter { stack ->
            state.block.asItem() == stack.item
                    && state.values[IBuiltInStove.NESTED_IN_STOVE] != null
                    && state.getValue(IBuiltInStove.NESTED_IN_STOVE)
        }.forEach { stack -> drops.add(ItemStack(IFDItems.INLAID_STOVE)) }
        return drops
    }

    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        if (canGet()) {
            var rP = pos
            if (this is IBedPartState) {
                rP = IBedPartState.getFootPos(state, pos);
            }
            if (BlockUtil.getThis(player, level, rP, state, InteractionHand.MAIN_HAND, true, true)) {
                val block = state.block
                if (block is IBuiltInStove && block.isNestedInStove(state)) {
                    BlockUtil.replaceBlockWithAllState(state, IFDBlocks.INLAID_STOVE.get().defaultBlockState(), level, pos);
                }
            }
        }
        super.attack(state, level, pos, player)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val origin = getShapeThis(state, level, pos, context)
        val block = state.block
        if (block is IBuiltInStove && block.isNestedInStove(state)) {
            return Shapes.or(origin.move(0.0, block.getYOffset(state), 0.0), block.getShape(state));
        }
        return origin
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        if (this is IBuiltInStove) {
            builder.add(IBuiltInStove.NESTED_IN_STOVE)
        }
    }

    override fun getRenderShape(pState: BlockState): RenderShape {
        if (this is IBuiltInStove) {
            return RenderShape.INVISIBLE
        }
        return super.getRenderShape(pState)
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        val block = state.block
        if (block is IBuiltInStove && block.isNestedInStove(state) && state.getValue(ILitState.LIT)) {
            InlaidStoveBlock.makeFire(state, level, pos, random);
            InlaidStoveBlock.playFireSound(level, pos, random);
        }
        super.animateTick(state, level, pos, random)
    }

    override fun hasDynamicLightEmission(state: BlockState): Boolean {
        return true
    }

    /**
     * 自定义光照，可在tick中调用AxuLightManager来让光照和内部液体/物品照度同步，当然和默认值之间取最大值
     */
    override fun getLightEmission(state: BlockState, level: BlockGetter, pos: BlockPos): Int {
        val origin = super.getLightEmission(state, level, pos)
        val manager = level.getAuxLightManager(pos) ?: return origin
        return manager.getLightAt(pos).takeIf { it > origin } ?: origin
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        lightSyncWithFluidIn(level, pos, state, blockEntity)
    }

    /**
     * 默认将容器底部所得的液体内容照度显示出来
     */
    open fun lightSyncWithFluidIn(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        val tank = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, blockEntity, Direction.DOWN) ?: return
        val fluidIn = tank.getFluidInTank(0)
        level.getAuxLightManager(pos)?.setLightAt(pos, fluidIn.fluidType.getLightLevel(fluidIn))
    }

    /**
     * 是否同步掉落物内容
     */
    open fun dropSync(): Boolean {
        return true
    }

    /**
     * 是否可蹲下快速拿起
     */
    open fun canGet(): Boolean {
        return true
    }

}