package cn.solarmoon.idyllic_food_diary.core.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.api.common.block.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SteamerBaseBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 蒸笼底座，能蓄水，把水烧开，开水会在热源上逐渐消耗
 */
public class SteamerBaseBlock extends BaseCookwareBlock {

    public SteamerBaseBlock(Properties properties) {
        super(properties);
    }

    public SteamerBaseBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.BAMBOO)
                .strength(2)
                .noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        SteamerBaseBlockEntity steamerBase = (SteamerBaseBlockEntity) level.getBlockEntity(pos);
        if (steamerBase == null) return InteractionResult.PASS;
        if (steamerBase.putFluid(player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            steamerBase.setChanged();
            return InteractionResult.SUCCESS;
        }
        if (steamerBase.takeFluid(player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
            steamerBase.setChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        SteamerBaseBlockEntity steamerBase = (SteamerBaseBlockEntity) blockEntity;
        steamerBase.tryBoilWater();
        steamerBase.tryDrainHotFluid();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        SteamerBaseBlockEntity base = (SteamerBaseBlockEntity) level.getBlockEntity(pos);
        if (base == null) return;
        if (base.isEvaporating()) {
            level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1, 0);
        } else if (base.isBoiling()) {
            if (random.nextInt(10) < 2) level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1, 0);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape[] shapes = new VoxelShape[] {
                Block.box(0.5, 0, 0.5, 15.5, 2, 15.5),
                Block.box(1, 2, 1, 15, 7, 15),
                Block.box(0.5, 7, 0.5, 15.5, 9, 15.5),
                Block.box(1, 9, 1, 15, 14, 15),
        };
        VoxelShape top = Block.box(0.5, 14, 0.5, 15.5, 16, 15.5);
        VoxelShape mix = Shapes.joinUnoptimized(top, Block.box(2, 15, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
        return Shapes.joinUnoptimized(Shapes.or(mix, shapes), Block.box(2, 0, 2, 14, 1, 14), BooleanOp.ONLY_FIRST);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STEAMER_BASE.get();
    }

}
