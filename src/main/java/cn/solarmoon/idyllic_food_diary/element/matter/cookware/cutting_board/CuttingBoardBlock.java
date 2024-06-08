package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.common.block.IBlockUseCaller;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.Event;

public class CuttingBoardBlock extends BaseCookwareBlock implements IBlockUseCaller {

    public CuttingBoardBlock() {
        super(Properties.copy(Blocks.CHEST).noOcclusion());
    }

    public CuttingBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        if (blockEntity instanceof CuttingBoardBlockEntity cb) {
            if (cb.trOutputResult(heldItem, player, pos, level)) {
                cb.addSpicesToItem(cb.getInventory().getStackInSlot(0), false);
                return InteractionResult.SUCCESS;
            }
            // 蹲下右键全拉出来
            if (player.isCrouching() && heldItem.isEmpty()) {
                cb.pumpOutAllItems(new Vec3(0, -0.5, 0));
                return InteractionResult.SUCCESS;
            }
            // 正常存
            if (cb.putItem(player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.PLAYERS);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
            if (cb.takeItem(player, hand, 1)) {
                level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.PLAYERS);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos blockPos, Level level, Player player, ItemStack itemStack, BlockHitResult blockHitResult, InteractionHand hand) {
        return Event.Result.ALLOW;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(0.0D, 0.0D, 1.0D, 16.0D, 1.0D, 15.0D));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.CUTTING_BOARD.get();
    }

}
