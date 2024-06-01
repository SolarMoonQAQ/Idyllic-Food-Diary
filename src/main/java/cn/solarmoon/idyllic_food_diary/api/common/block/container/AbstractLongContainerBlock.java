package cn.solarmoon.idyllic_food_diary.api.common.block.container;

import cn.solarmoon.solarmoon_core.api.common.block.BaseWaterBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AbstractLongContainerBlock extends AbstractContainerBlock implements IBedPartBlock {

    public AbstractLongContainerBlock(SoundType soundType) {
        super(BlockBehaviour.Properties.of()
                .strength(1)
                .sound(soundType)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion()
        );
    }

}
