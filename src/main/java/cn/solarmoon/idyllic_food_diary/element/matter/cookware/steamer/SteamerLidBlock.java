package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.solarmoon_core.api.block_base.BaseBlock;
import cn.solarmoon.solarmoon_core.api.block_base.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IWaterLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamerLidBlock extends BaseBlock implements IWaterLoggedBlock {

    public SteamerLidBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.BAMBOO).strength(0.7F).noOcclusion());
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
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(1, 0, 1, 15, 2, 15);
    }

}
