package cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock
import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlockEntities
import cn.solarmoon.idyllic_food_diary.registry.common.IFDParticles
import cn.solarmoon.idyllic_food_diary.registry.common.IFDSounds
import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.cap.item.ItemStackHandlerHelper
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.FluidUtil
import java.util.Random

class KettleBlock: CookwareBlock(Properties.of().sound(SoundType.LANTERN).strength(2f).forceSolidOn()) {

    override fun useItemOnThis(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        val kettle = level.getBlockEntity(pos) as KettleBlockEntity
        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction)) return ItemInteractionResult.sidedSuccess(level.isClientSide)
        if (ItemStackHandlerHelper.storage(kettle.inventory, player, hand, 1, 1)) {
            level.playSound(null, pos, getSoundType(state, level, pos, null).hitSound, SoundSource.PLAYERS)
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: BlockEntity) {
        super.tick(level, pos, state, blockEntity)
        val kettle = blockEntity as KettleBlockEntity

        if (!kettle.boil.tryWork()) kettle.brew.tryWork()
        makeBoilParticle(level, pos, state, kettle)
    }

    fun makeBoilParticle(level: Level, pos: BlockPos, state: BlockState, kettle: KettleBlockEntity) {
        val face = state.getValue(IHorizontalFacingState.FACING);
        val delta1 = -face.stepZ * 0.5
        val delta2 = face.stepX * 0.5
        val spoutPos = pos.center.add(delta1, 0.07, delta2);
        val random = Random()
        if (kettle.boil.isInBoil()) {
            if (random.nextFloat() < 0.8) {
                level.addAlwaysVisibleParticle(IFDParticles.CRASHLESS_CLOUD.get(), spoutPos.x, spoutPos.y, spoutPos.z, 0.0, 0.1, 0.0);
            }
            if (random.nextFloat() < 0.1) {
                level.playSound(null, pos, IFDSounds.BOILING_WATER.get(), SoundSource.BLOCKS)
            }
        }
        else if (kettle.boil.isBoiling()) {
            if (random.nextFloat() < 0.1)
                level.addAlwaysVisibleParticle(IFDParticles.CRASHLESS_CLOUD.get(), spoutPos.x, spoutPos.y, spoutPos.z, 0.0, 0.01, 0.0);
        }
    }

    override fun getShapeThis(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape {
        return box(4.0, 0.0, 4.0, 12.0, 7.0, 12.0)
    }

    override fun getBlockEntityType(): BlockEntityType<*> {
        return IFDBlockEntities.KETTLE.get()
    }

}