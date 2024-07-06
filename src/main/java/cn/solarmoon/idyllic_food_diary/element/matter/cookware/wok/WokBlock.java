package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.CookwareBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.renderer.IFreeRenderBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedEntityBlock;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WokBlock extends CookwareBlock implements IHorizontalFacingBlock, IFreeRenderBlock, IBuiltInStove {

    public WokBlock() {
        super(Block.Properties.of()
                .sound(SoundType.LANTERN)
                .strength(2f, 6.0F)
                .noOcclusion());
    }

    public WokBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult originUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        WokBlockEntity pan = (WokBlockEntity) level.getBlockEntity(pos);
        if (pan == null) return super.use(state, level, pos, player, hand, hitResult);

        if (pan.tryGiveResult(player, hand)) {
            return InteractionResult.SUCCESS;
        }

        if (level.isClientSide && player.getMainHandItem().is(IMItems.SPATULA.get()) && pan.doStirFry()) {
            return InteractionResult.SUCCESS;
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!pan.hasResult() && !pan.canStirFry()) {
            //能够存取液体
            if (FluidHandlerUtil.loadFluid(pan.getTank(), player, hand, true)) {
                return InteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand.equals(InteractionHand.MAIN_HAND) && !player.getMainHandItem().is(IMItems.SPATULA.get())
                    && ItemHandlerUtil.storage(pan.getInventory(), player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof WokBlockEntity pan) {
            pan.tryStirFrying();
            pan.tryApplyThermochanger();

            // 炒菜音效
            if (pan.isStirFrying() && level.isClientSide) {
                if (pan.soundTick == 0 || pan.soundTick > 90) {
                    level.playLocalSound(pos, IMSounds.STIR_FRY.get(), SoundSource.BLOCKS, 1, 1, false);
                    pan.soundTick = 1;
                }
                pan.soundTick++;
            } else {
                pan.soundTick = 0;
            }

            // 嵌入炉灶可以蒸水
            if (isNestedInStove(state)) pan.tryDrainHotFluid();

        }
        super.tick(level, pos, state, blockEntity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        WokBlockEntity pan = (WokBlockEntity) level.getBlockEntity(pos);
        // 炒菜时的锅气
        if (pan != null) {
            if (pan.isStirFrying()) {
                double rInRange = 2/16f + random.nextDouble() * 12/16; // 保证粒子起始点在锅内
                double vi = (random.nextDouble() - 0.5) / 5;
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + rInRange, pos.getY() + 1 / 16f + getYOffset(), pos.getZ() + rInRange, vi, 0.1, vi);
            }
        }
    }

    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape body = Shapes.joinUnoptimized(
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(3, 1, 3, 13, 4, 13),
                BooleanOp.ONLY_FIRST
        );
        VoxelShape handle = Shapes.or(
                Block.box(0, 2, 5, 2, 4, 11),
                Block.box(14, 2, 5, 16, 4, 11)
        );
        return VoxelShapeUtil.rotateShape(state.getValue(FACING), Shapes.or(body, handle));
    }

    @Override
    public double getYOffset() {
        return 12 / 16f;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.WOK.get();
    }

}
