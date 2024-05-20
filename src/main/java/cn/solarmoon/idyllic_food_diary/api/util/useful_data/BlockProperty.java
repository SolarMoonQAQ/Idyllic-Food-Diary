package cn.solarmoon.idyllic_food_diary.api.util.useful_data;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class BlockProperty {

    public static BlockBehaviour.Properties DOUGH = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.WOOL)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_ON_CONTAINER = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.WOOL)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_ON_LARGE_CONTAINER = BlockBehaviour.Properties.of()
            .strength(1.25F)
            .sound(SoundType.WOOL)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);

    public static BlockBehaviour.Properties FOOD_IN_BOWL = BlockBehaviour.Properties.of()
            .strength(0.5F)
            .sound(SoundType.BAMBOO)
            .noOcclusion();

}
