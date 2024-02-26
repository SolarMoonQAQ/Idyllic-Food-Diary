package cn.solarmoon.immersive_delight.common.block.entity_block;

import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractSteamerEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SteamerEntityBlock extends AbstractSteamerEntityBlock {

    public SteamerEntityBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(1).noOcclusion());
    }

}
