package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LittleCupBlock extends AbstractCupBlock {

    protected static final VoxelShape[] SHAPE = new VoxelShape[]{
            Block.box(6.5D, 0.0D, 6.5D, 9.5D, 0.5D, 9.5D),
            Block.box(6.0D, 0.5D, 6.0D, 10.0D, 4.5D, 10.0D)
    };

    public LittleCupBlock() {
        super(Block.Properties.of()
                .sound(SoundType.GLASS)
                .strength(0.5f)
                .noOcclusion());
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.or(SHAPE[0], SHAPE[1]);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.LITTLE_CUP.get();
    }

}
