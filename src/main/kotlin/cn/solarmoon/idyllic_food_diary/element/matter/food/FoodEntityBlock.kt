package cn.solarmoon.idyllic_food_diary.element.matter.food

import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.IInteraction
import cn.solarmoon.idyllic_food_diary.feature.food_container.FoodContainer
import cn.solarmoon.idyllic_food_diary.registry.common.IFDAttachments
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.blockentity.HandyEntityBlock
import cn.solarmoon.spark_core.api.blockentity.SyncedEntityBlock
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import test.be

abstract class FoodEntityBlock(properties: Properties): HandyEntityBlock(properties), IHorizontalFacingState {

    companion object {
        @JvmStatic
        val INTERACTION = IntegerProperty.create("interaction", 0, 12)
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(INTERACTION, maxInteraction))
    }

    abstract val maxInteraction: Int

    abstract fun getFoodShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape

    /**
     * 自由设定每一阶段的独特交互
     */
    abstract fun getInteraction(stage: Int): IInteraction

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val stage = state.getValue(INTERACTION)
        if (getInteraction(stage).doInteraction(stack, state, level, pos, player, hand, hitResult)) return ItemInteractionResult.sidedSuccess(level.isClientSide)
        return ItemInteractionResult.CONSUME // 防止右键此类方块时使用手中物品
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)
        val be = level.getBlockEntity(pos) ?: return
        be.applyComponentsFromItemStack(stack)
        level.setBlock(pos, state.setValue(INTERACTION, be.components().getOrDefault(IFDDataComponents.INTERACTION.get(), maxInteraction)), 3)
    }

    override fun getCloneItemStack(
        state: BlockState,
        target: HitResult,
        level: LevelReader,
        pos: BlockPos,
        player: Player
    ): ItemStack {
        val origin = super.getCloneItemStack(state, target, level, pos, player)
        val be = level.getBlockEntity(pos) ?: return origin
        origin.applyComponents(be.collectComponents())
        return origin
    }

    override fun getDrops(state: BlockState, params: LootParams.Builder): List<ItemStack?> {
        val drops = super.getDrops(state, params)
        val be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) ?: return drops
        val item = asItem().defaultInstance
        item.applyComponents(be.components())
        drops.add(item)
        return drops
    }

    override fun getSoundType(state: BlockState, level: LevelReader, pos: BlockPos, entity: Entity?): SoundType {
        val origin = super.getSoundType(state, level, pos, entity)
        val be = level.getBlockEntity(pos)
        if (be is FoodBlockEntity && !be.containerBlockState.isAir) return be.containerBlockState.getSoundType(level, pos, entity)
        return origin
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val be = level.getBlockEntity(pos)
        var shapeContainer = Shapes.empty()
        if (be is FoodBlockEntity) shapeContainer = be.containerBlockState.getShape(level, pos)
        return Shapes.or(shapeContainer, getFoodShape(state, level, pos, context))
    }

    override fun getAnalogOutputSignal(state: BlockState, level: Level, pos: BlockPos): Int {
        return (state.getValue(INTERACTION)/maxInteraction.toDouble() * 15).toInt()
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(INTERACTION)
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.FOOD.get()
    }

}