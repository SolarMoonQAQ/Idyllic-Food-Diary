package cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove;

import cn.solarmoon.solarmoon_core.api.block_base.BaseBlock;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.phys.VecUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class InlaidStoveBlock extends BaseBlock implements ILitBlock, IHorizontalFacingBlock {

    public InlaidStoveBlock() {
        super(Properties.of()
                .sound(SoundType.STONE)
                .strength(1.5f)
                .lightLevel((state) -> state.getValue(LIT) ? 13 : 0)
        );
    }

    public static boolean isClickInStove(HitResult hitResult, BlockPos pos) {
        return VecUtil.isInside(hitResult.getLocation(), pos, 2 / 16f, 8 / 16f, 2 / 16f, 14 / 16f, 16 / 16f, 14 / 16f, true);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);

        //打火石等点燃和熄灭
        if (ILitBlock.controlLitByHand(state, pos, level, player, hand)) {
            return InteractionResult.SUCCESS;
        }

        // 放入可镶嵌厨具
        if (isClickInStove(hitResult, pos)) {
            Block cookware = Block.byItem(heldItem.getItem());
            if (cookware instanceof IBuiltInStove) {
                BlockState d = cookware.getStateForPlacement(new BlockPlaceContext(player, hand, heldItem, hitResult));
                if (d != null) {
                    BlockUtil.replaceBlockWithAllState(state, d.setValue(IBuiltInStove.NESTED_IN_STOVE, true), level, pos);
                    cookware.setPlacedBy(level, pos, level.getBlockState(pos), player, heldItem);
                    level.playSound(null, pos, d.getSoundType().getPlaceSound(), SoundSource.BLOCKS);
                    if (!player.isCreative()) heldItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static void makeFire(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
        Direction.Axis direction$axis = direction.getAxis();
        double horizontalOffset = random.nextDouble() * 0.6 - 0.3;
        double xOffset = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52 : horizontalOffset;
        double yOffset = random.nextDouble() * 6.0 / 16.0;
        double zOffset = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : horizontalOffset;
        level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0, 0.0, 0.0);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(LIT)) makeFire(state, level, pos, random);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.joinUnoptimized(Shapes.block(), box(2, 8, 2, 14, 16, 14), BooleanOp.ONLY_FIRST);
    }

}
