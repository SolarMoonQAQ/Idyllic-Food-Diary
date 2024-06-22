package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.property.Properties;

/**
 * 汤锅
 */
public class CookingPotBlock extends BaseCookwareBlock {

    public CookingPotBlock() {
        super(Block.Properties.of()
                .sound(SoundType.LANTERN)
                .strength(2f, 6.0F)
                .noOcclusion());
    }

    public CookingPotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        CookingPotBlockEntity cookingPot = (CookingPotBlockEntity) level.getBlockEntity(pos);
        if (cookingPot == null) return InteractionResult.PASS;
        if (cookingPot.tryGiveResult(player, hand)) {
            return InteractionResult.SUCCESS;
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!cookingPot.hasResult()) {
            //能够存取液体
            if (cookingPot.putFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }
            if (cookingPot.takeFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand.equals(InteractionHand.MAIN_HAND) && cookingPot.storage(player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                cookingPot.setChanged();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        getThis(player, level, pos, state, InteractionHand.MAIN_HAND, true);
        super.attack(state, level, pos, player);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        CookingPotBlockEntity pot = (CookingPotBlockEntity) blockEntity;
        if (!pot.tryStew()) {
            if (!pot.trySimmer()) {
                if (!pot.tryBoilFood()) {
                    if (!pot.tryBoilWater()) {

                    }
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity soupPot) {
            if (soupPot.isHeatingFluid()) {
                if (random.nextInt(10) < 4) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CLOUD, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), 0, 0.1, 0);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape1 = Block.box(1.0D, 0.0D, 1.0D, 15D, 16D, 15D);
        VoxelShape shape2 = Block.box(3, 1, 3, 13, 16, 13);
        return Shapes.joinUnoptimized(shape1, shape2, BooleanOp.ONLY_FIRST);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.COOKING_POT.get();
    }

}
