package cn.solarmoon.idyllic_food_diary.common.block.food_block.long_press_eat_block;

import cn.solarmoon.idyllic_food_diary.common.block.base.AbstractLongPressEatFoodBlock;
import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
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
    public Block getBlockLeft() {
        return IMBlocks.BOWL.get();
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
