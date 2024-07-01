package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot;

import cn.solarmoon.idyllic_food_diary.registry.client.IMParticles;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 汤锅
 */
public class CookingPotBlock extends SyncedEntityBlock implements IHorizontalFacingBlock {

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
        ItemStackHandler inv = cookingPot.getInventory();
        FluidTank tank = cookingPot.getTank();
        if (cookingPot.tryGiveResult(player, hand)) {
            return InteractionResult.SUCCESS;
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!cookingPot.hasResult()) {
            //能够存取液体
            if (FluidHandlerUtil.putFluid(tank, player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
                return InteractionResult.SUCCESS;
            }
            if (FluidHandlerUtil.takeFluid(tank, player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                return InteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand.equals(InteractionHand.MAIN_HAND) && ItemHandlerUtil.storage(inv, player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
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
        pot.tryApplyThermochanger();
        if (!pot.tryStew()) {
            if (!pot.trySimmer()) {
                if (!pot.tryBoilFood()) {
                    pot.tryBoilWater();
                }
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CookingPotBlockEntity soupPot) {
            if (soupPot.isStewing() || soupPot.isSimmering() || soupPot.isBoilingFood() || soupPot.isBoiling() || soupPot.isInBoil()) {
                if (random.nextInt(10) < (soupPot.isInBoil() ? 10 : 4)) {
                    float rInRange = 4 / 16f + random.nextFloat() * 8 / 16f;
                    level.addAlwaysVisibleParticle(IMParticles.CRASHLESS_CLOUD.get(), pos.getX() + rInRange, pos.getY() + 1, pos.getZ() + rInRange, 0, 0.1, 0);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape1 = Block.box(2.0D, 0.0D, 2.0D, 14D, 13D, 14D);
        VoxelShape shape2 = Block.box(4, 2, 4, 12, 13, 12);
        VoxelShape shapeBody = Shapes.joinUnoptimized(shape1, shape2, BooleanOp.ONLY_FIRST);
        VoxelShape shapeHandle = Shapes.or(
                Block.box(0, 9, 5, 2, 11, 11),
                Block.box(14, 9, 5, 16, 11, 11)
        );
        return VoxelShapeUtil.rotateShape(state.getValue(FACING), Shapes.or(shapeBody, shapeHandle));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.COOKING_POT.get();
    }

}
