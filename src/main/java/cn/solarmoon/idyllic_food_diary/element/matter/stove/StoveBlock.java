package cn.solarmoon.idyllic_food_diary.element.matter.stove;

import cn.solarmoon.idyllic_food_diary.element.matter.stove.water_storage_stove.WaterStorageStoveBlockEntity;
import cn.solarmoon.solarmoon_core.api.block_base.BaseBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StoveBlock extends BaseBlock implements ILitBlock {

    public StoveBlock() {
        super(Properties.of()
                .sound(SoundType.STONE)
                .strength(1.5f)
                .lightLevel((state) -> state.getValue(LIT) ? 13 : 0)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);

        //打火石等点燃
        if (!state.getValue(LIT)) {
            if (heldItem.getItem() instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, player.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldItem.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }
        }

        // 放入炼药锅变为蓄水灶台

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

}
