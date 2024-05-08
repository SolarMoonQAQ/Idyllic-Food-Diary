package cn.solarmoon.idyllic_food_diary.api.util.useful_data;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class BlockProperty {

    public static BlockBehaviour.Properties DOUGH = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.WOOL)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_ON_CHINA_PLATE = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.GLASS)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_ON_WOODEN_PLATE = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.BAMBOO)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_IN_BOWL = BlockBehaviour.Properties.of()
            .strength(0.5F)
            .sound(SoundType.BAMBOO)
            .noOcclusion();

    public static BlockBehaviour.Properties FOOD_ON_DOUBLE_CHINA_PLATE = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);

    public static BlockBehaviour.Properties FOOD_ON_DOUBLE_WOODEN_PLATE = BlockBehaviour.Properties.of()
            .strength(1)
            .sound(SoundType.BAMBOO)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);

}
