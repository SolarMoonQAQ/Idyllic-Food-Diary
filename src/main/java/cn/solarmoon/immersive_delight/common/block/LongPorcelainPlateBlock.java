package cn.solarmoon.immersive_delight.common.block;

import cn.solarmoon.solarmoon_core.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LongPorcelainPlateBlock extends BaseWaterBlock implements IBedPartBlock {

    public LongPorcelainPlateBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(1)
                .sound(SoundType.GLASS)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
        );
    }

    /**
     * 可以快速拿起
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
       if (getThis(player, level, pos, state, hand)) {
           level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1);
           return InteractionResult.SUCCESS;
       }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
