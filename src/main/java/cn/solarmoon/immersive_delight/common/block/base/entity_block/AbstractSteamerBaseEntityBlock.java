package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractSteamerBaseBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseTankEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 蒸笼底座，能蓄水，把水烧开，开水会在热源上逐渐消耗
 */
public abstract class AbstractSteamerBaseEntityBlock extends BaseTankEntityBlock {

    public AbstractSteamerBaseEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        AbstractSteamerBaseBlockEntity steamerBase = (AbstractSteamerBaseBlockEntity) level.getBlockEntity(pos);
        if (steamerBase == null) return InteractionResult.PASS;
        if (putFluid(steamerBase, player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS);
            steamerBase.setChanged();
            return InteractionResult.SUCCESS;
        }
        if (takeFluid(steamerBase, player, hand, false)) {
            level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS);
            steamerBase.setChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        AbstractSteamerBaseBlockEntity steamerBase = (AbstractSteamerBaseBlockEntity) blockEntity;
        steamerBase.tryBoilWater();
        steamerBase.tryDrainHotFluid();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.STEAMER_BASE.get();
    }

}
