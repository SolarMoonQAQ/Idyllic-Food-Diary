package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast;

import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.AbstractInteractiveFoodBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.matcher.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EastSlopePorkBlock extends AbstractInteractiveFoodBlock {

    public EastSlopePorkBlock() {
        super(BlockProperty.FOOD_ON_MEDIUM_CONTAINER);
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ObtainInteraction interaction = new ObtainInteraction(
                new ItemStack(Items.PORKCHOP),
                ItemMatcher.empty(),
                ObtainInteraction.DropForm.INVENTORY,
                ObtainInteraction.ObtainingMethod.SERVE
        );
        return Either.left(interaction);
    }

    @Override
    public int getMaxInteraction() {
        return 4;
    }

    @Override
    public VoxelShape getOriginShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

}
