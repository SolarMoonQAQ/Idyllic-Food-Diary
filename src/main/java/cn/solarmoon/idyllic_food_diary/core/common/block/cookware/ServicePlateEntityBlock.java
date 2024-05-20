package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.cookware.AbstractServicePlateEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ServicePlateEntityBlock extends AbstractServicePlateEntityBlock {

    public ServicePlateEntityBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.GLASS)
                .strength(0.7F)
                .noOcclusion()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        // AbstractServicePlateBlockEntity plate = (AbstractServicePlateBlockEntity) getter.getBlockEntity(pos);
        // if (plate == null) return Shapes.block();
        // int add = plate.getStacks().size(); <- 想让堆叠的食物也具有碰撞箱，但是堆叠太高了先不改
        return Block.box(1, 0, 1, 15, 2, 15);
    }

}
