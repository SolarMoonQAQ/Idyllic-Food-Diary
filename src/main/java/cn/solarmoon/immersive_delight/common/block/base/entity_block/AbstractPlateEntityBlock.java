package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractPlateBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AbstractPlateEntityBlock extends BaseContainerEntityBlock {

    public AbstractPlateEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        AbstractPlateBlockEntity plate = (AbstractPlateBlockEntity) level.getBlockEntity(pos);
        if (plate == null) return InteractionResult.PASS;
        if (storage(plate, player, hand)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.PLATE.get();
    }

}
