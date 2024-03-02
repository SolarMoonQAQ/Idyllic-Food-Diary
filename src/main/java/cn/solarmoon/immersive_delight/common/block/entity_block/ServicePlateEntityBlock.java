package cn.solarmoon.immersive_delight.common.block.entity_block;

import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractServicePlateEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ServicePlateEntityBlock extends AbstractServicePlateEntityBlock {

    public ServicePlateEntityBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.GLASS));
    }

}
