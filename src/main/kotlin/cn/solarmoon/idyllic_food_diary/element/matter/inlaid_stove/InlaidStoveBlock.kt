package cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove

import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.ILitState
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.VecUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class InlaidStoveBlock(properties: Properties = Properties.of()
    .sound(SoundType.STONE)
    .strength(1.5f)
    .lightLevel { state -> if (state.getValue(ILitState.LIT)) 13 else 0 }) : Block(properties), ILitState, IHorizontalFacingState {

    override fun useItemOn(
        heldItem: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        //打火石等点燃和熄灭
        if (ILitState.controlLitByHand(state, pos, level, player, hand)) {
            return ItemInteractionResult.SUCCESS;
        }

        // 放入可镶嵌厨具
        if (isClickInStove(hitResult, pos)) {
            val cookware = byItem(heldItem.item)
            if (cookware is IBuiltInStove) {
                val d = cookware.getStateForPlacement(BlockPlaceContext(player, hand, heldItem, hitResult))
                if (d != null) {
                    BlockUtil.replaceBlockWithAllState(state, d.setValue(IBuiltInStove.NESTED_IN_STOVE, true), level, pos)
                    cookware.setPlacedBy(level, pos, level.getBlockState(pos), player, heldItem)
                    level.playSound(null, pos, d.getSoundType(level, pos, null).placeSound, SoundSource.BLOCKS)
                    if (!player.isCreative) heldItem.shrink(1)
                }
                return ItemInteractionResult.SUCCESS
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        super.animateTick(state, level, pos, random)
        if (state.getValue(ILitState.LIT)) {
            playFireSound(level, pos, random)
            makeFire(state, level, pos, random)
        }
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return Shapes.joinUnoptimized(Shapes.block(), box(2.0, 8.0, 2.0, 14.0, 16.0, 14.0), BooleanOp.ONLY_FIRST);
    }

    companion object {
        @JvmStatic
        fun isClickInStove(hitResult: HitResult, pos: BlockPos): Boolean {
            return VecUtil.isInside(hitResult.getLocation(), pos, 2 / 16.0, 8 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0, true);
        }

        @JvmStatic
        fun makeFire(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
            val x = pos.x + 0.5
            val y = pos.y
            val z = pos.z + 0.5
            val direction = state.getValue(IHorizontalFacingState.FACING)
            val directionAxis = direction.axis
            val horizontalOffset = random.nextDouble() * 0.6 - 0.3
            val xOffset = if (directionAxis == Direction.Axis.X) direction.stepX * 0.52 else horizontalOffset
            val yOffset = random.nextDouble() * 6.0 / 16.0
            val zOffset = if (directionAxis == Direction.Axis.Z) direction.stepZ * 0.52 else horizontalOffset
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0)
            level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0)
        }

        @JvmStatic
        fun playFireSound(level: Level, pos: BlockPos, random: RandomSource) {
            val x = pos.x + 0.5
            val y = pos.y.toDouble()
            val z = pos.z + 0.5
            if (random.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

}
