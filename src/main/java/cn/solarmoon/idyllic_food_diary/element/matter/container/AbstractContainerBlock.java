package cn.solarmoon.idyllic_food_diary.element.matter.container;

import cn.solarmoon.solarmoon_core.api.block_base.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IWaterLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractContainerBlock extends BaseWaterBlock implements IHorizontalFacingBlock, IWaterLoggedBlock {

    public AbstractContainerBlock(SoundType soundType) {
        super(BlockBehaviour.Properties.of()
                .sound(soundType)
                .strength(0.7F)
                .noOcclusion()
        );
    }

    public AbstractContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        getThis(player, level, pos, state, InteractionHand.MAIN_HAND, true);
        super.attack(state, level, pos, player);
    }

}
