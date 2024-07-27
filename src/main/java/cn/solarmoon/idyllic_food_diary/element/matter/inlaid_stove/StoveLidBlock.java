package cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove;

import cn.solarmoon.solarmoon_core.api.block_base.BaseBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IWaterLoggedBlock;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StoveLidBlock extends BaseBlock implements IWaterLoggedBlock {

    public StoveLidBlock() {
        super(Properties.copy(Blocks.OAK_PLANKS));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (hand == InteractionHand.MAIN_HAND && heldItem.isEmpty()) {
            if (getThis(player, level, pos, state, hand, false, true)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction direction = state.getValue(FACING);
        VoxelShape base = Block.box(1.5, 0, 1.5, 14.5, 1, 14.5);
        VoxelShape add = Block.box(2, 1, 7, 14, 2, 9);
        return VoxelShapeUtil.rotateShape(direction, Shapes.or(base, add));
    }

}
