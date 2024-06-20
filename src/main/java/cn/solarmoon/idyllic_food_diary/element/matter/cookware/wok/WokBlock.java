package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.BaseCookwareBlock;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WokBlock extends BaseCookwareBlock {

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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        WokBlockEntity pan = (WokBlockEntity) level.getBlockEntity(pos);
        if (pan == null) return super.use(state, level, pos, player, hand, hitResult);

        if (pan.tryGiveResult(player, hand)) {
            pan.setChanged();
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide && pan.doStirFry()) {
            return InteractionResult.SUCCESS;
        }

        // 没有预输入结果时才能进行物品流体的交互
        if (!pan.hasResult() && !pan.canStirFry()) {
            //能够存取液体
            if (pan.putFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
                pan.setChanged();
                return InteractionResult.SUCCESS;
            }
            if (pan.takeFluid(player, hand, false)) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
                pan.setChanged();
                return InteractionResult.SUCCESS;
            }

            //存取任意单个物品
            if (hand.equals(InteractionHand.MAIN_HAND) && pan.storage(player, hand, 1, 1)) {
                level.playSound(null, pos, SoundEvents.LANTERN_STEP, SoundSource.BLOCKS);
                if (!level.isClientSide) IMPacks.CLIENT_PACK.getSender().pos(pos).send("t");
                pan.setChanged();
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof WokBlockEntity pan) {
            pan.tryStirFrying();
        }
        super.tick(level, pos, state, blockEntity);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.WOK.get();
    }

}
