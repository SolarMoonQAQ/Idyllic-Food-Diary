package cn.solarmoon.immersive_delight.common.block.long_press_eat_block;

import cn.solarmoon.immersive_delight.common.block.base.AbstractLongPressEatFoodBlock;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BowlSoupBlock extends AbstractLongPressEatFoodBlock {

    public BowlSoupBlock() {
        super(Block.Properties.of()
                .sound(SoundType.BAMBOO)
                .strength(0.5f)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (getThis(player, level, pos, state, hand)) return InteractionResult.SUCCESS;
        return super.use(state, level, pos, player, hand, hit);
    }

    /**
     * 把方块快速拿到空手里
     */
    public boolean getThis(Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack copy = getCloneItemStack(level, pos, state);
            level.removeBlock(pos, false);
            level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            player.setItemInHand(hand, copy);
            return true;
        }
        return false;
    }

    @Override
    public Block getBlockLeft() {
        return IMBlocks.TIN_FOIL_BOX.get();
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    }

}
