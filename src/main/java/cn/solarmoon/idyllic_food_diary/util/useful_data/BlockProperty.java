package cn.solarmoon.idyllic_food_diary.util.useful_data;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockProperty {

    public static BlockBehaviour.Properties DOUGH = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.WOOL)
            .noOcclusion();

}
