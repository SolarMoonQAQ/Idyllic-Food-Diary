package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cleaver.CleaverItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar.SpiceJarItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.block_use_caller.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.items.ItemStackHandler;

public class CuttingBoardBlock extends CookwareBlock implements IBlockUseCaller {

    public CuttingBoardBlock() {
        super(Properties.copy(Blocks.CHEST)
                .noOcclusion()
        );
    }

    public CuttingBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack heldItem = player.getItemInHand(hand);
        if (blockEntity instanceof CuttingBoardBlockEntity cb) {
            ItemStackHandler inv = cb.getInventory();
            if (cb.trOutputResult(heldItem, player, pos, level)) {
                return InteractionResult.SUCCESS;
            }
            // 蹲下右键全拉出来
            if (hand == InteractionHand.MAIN_HAND) {
                if (player.isCrouching() && heldItem.isEmpty()) {
                    ItemHandlerUtil.pumpOutAllItems(inv, cb, new Vec3(0, -0.5, 0));
                    return InteractionResult.SUCCESS;
                }
                // 正常存
                if (ItemHandlerUtil.putItem(inv, player, hand, 1)) {
                    level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.PLAYERS);
                    return InteractionResult.SUCCESS;
                }
                if (ItemHandlerUtil.takeItem(inv, player, hand, 1)) {
                    level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.PLAYERS);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos blockPos, Level level, Player player, ItemStack stack, BlockHitResult blockHitResult, InteractionHand hand) {
        Item item = stack.getItem();
        if (player.isCrouching() && !player.getItemInHand(hand).isEmpty()) return Event.Result.DENY;
        return item instanceof SpiceJarItem || item instanceof CleaverItem ? Event.Result.DENY : Event.Result.ALLOW;
    }

    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VoxelShapeUtil.rotateShape(state.getValue(FACING),
                Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.CUTTING_BOARD.get();
    }

}
