package cn.solarmoon.immersive_delight.common.entity_blocks;

import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.api.common.entity_block.AbstractSoupPotEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;

public class SoupPotEntityBlock extends AbstractSoupPotEntityBlock {

    public SoupPotEntityBlock() {
        super(Block.Properties.of()
                .sound(SoundType.STONE)
                .strength(1.0f, 6.0F)
                .mapColor(MapColor.METAL));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMEntityBlocks.SOUP_POT_ENTITY.get();
    }
}
