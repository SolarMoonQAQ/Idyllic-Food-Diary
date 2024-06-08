package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block;

import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.util.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.util.device.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedSalmonBlock extends AbstractInteractiveFoodBlock implements IBedPartBlock {

    public SteamedSalmonBlock() {
        super(BlockProperty.FOOD_ON_LARGE_CONTAINER);
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ConsumeInteraction consume = new ConsumeInteraction(
                3,
                new FoodProperties.Builder()
                        .nutrition(6).saturationMod(0.5f)
                        .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 600, 0), 1)
                        .build()
        );
        ObtainInteraction obtain = new ObtainInteraction(
                new ItemStack(Items.BONE, 2),
                ItemMatcher.any(),
                ObtainInteraction.DropForm.DROP,
                ObtainInteraction.ObtainingMethod.SPLIT
        );
        if (stageIndex == 1) return Either.left(obtain);
        return Either.right(consume);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public int getMaxInteraction() {
        return 5;
    }

}
