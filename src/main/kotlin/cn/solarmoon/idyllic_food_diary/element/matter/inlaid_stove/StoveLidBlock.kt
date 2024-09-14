package cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove

import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.IWaterLoggedState
import cn.solarmoon.spark_core.api.util.BlockUtil
import cn.solarmoon.spark_core.api.util.VoxelShapeUtil
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class StoveLidBlock(properties: Properties = Properties.ofFullCopy(Blocks.OAK_PLANKS)): Block(properties), IWaterLoggedState, IHorizontalFacingState {

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (BlockUtil.getThis(player, level, pos, state, InteractionHand.MAIN_HAND, false, true)) {
            return InteractionResult.SUCCESS
        }
        return InteractionResult.FAIL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        val direction = state.getValue(IHorizontalFacingState.FACING)
        val base = box(1.5, 0.0, 1.5, 14.5, 1.0, 14.5)
        val add = box(2.0, 1.0, 7.0, 14.0, 2.0, 9.0)
        return VoxelShapeUtil.rotateShape(direction, Shapes.or(base, add));
    }

}