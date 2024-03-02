package cn.solarmoon.immersive_delight.common.block.base;

import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public abstract class AbstractLongPressEatFoodDoubleBlock extends AbstractLongPressEatFoodBlock implements IBedPartBlock {
    public AbstractLongPressEatFoodDoubleBlock() {
        super( BlockBehaviour.Properties.of()
                .strength(1)
                .sound(SoundType.WOOL)
                .noCollission()
                .pushReaction(PushReaction.DESTROY));
    }
}
