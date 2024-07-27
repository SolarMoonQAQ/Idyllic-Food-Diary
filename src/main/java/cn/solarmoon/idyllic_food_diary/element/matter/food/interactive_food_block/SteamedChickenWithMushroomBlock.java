package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate.PlateBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.matcher.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedChickenWithMushroomBlock extends AbstractInteractiveFoodBlock {

    public SteamedChickenWithMushroomBlock() {
        super(BlockProperty.FOOD_ON_MEDIUM_CONTAINER);
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ObtainInteraction obtain = new ObtainInteraction(
                new ItemStack(IMItems.STEAMED_CHICKEN_WITH_MUSHROOM_BOWL.get()),
                ItemMatcher.of(Items.BOWL),
                ObtainInteraction.DropForm.INVENTORY,
                ObtainInteraction.ObtainingMethod.SERVE
        );
        return Either.left(obtain);
    }

    @Override
    public int getMaxInteraction() {
        return 3;
    }

    public static final VoxelShape SHAPE = Shapes.or(PlateBlock.SHAPE, Block.box(4, 1 , 4, 12, 6, 12));
    @Override
    public VoxelShape getOriginShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

}
