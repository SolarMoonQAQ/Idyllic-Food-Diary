package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.spark_core.api.blockstate.IHorizontalFacingState
import cn.solarmoon.spark_core.api.blockstate.IWaterLoggedState
import cn.solarmoon.spark_core.api.util.BlockUtil
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class SteamerLidBlock: Block(Properties.of()
    .sound(SoundType.BAMBOO).strength(0.7F).noOcclusion()), IHorizontalFacingState, IWaterLoggedState {

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        if (BlockUtil.getThis(player, level, pos, state, InteractionHand.MAIN_HAND, false, true)) {
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        return InteractionResult.FAIL
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return box(1.0, 0.0, 1.0, 15.0, 2.0, 15.0)
    }

}