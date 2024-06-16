package cn.solarmoon.idyllic_food_diary.element.matter.container;

import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

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
