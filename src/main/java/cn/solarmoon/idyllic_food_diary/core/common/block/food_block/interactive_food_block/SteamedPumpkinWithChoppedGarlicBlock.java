package cn.solarmoon.idyllic_food_diary.core.common.block.food_block.interactive_food_block;

import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.AbstractInteractiveFoodBlock;
import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.api.common.block.food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.api.util.useful_data.BlockProperty;
import cn.solarmoon.idyllic_food_diary.core.common.block.container.PorcelainPlateBlock;
import cn.solarmoon.idyllic_food_diary.core.common.block.container.WoodenPlateBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.api.util.device.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamedPumpkinWithChoppedGarlicBlock extends AbstractInteractiveFoodBlock {

    public SteamedPumpkinWithChoppedGarlicBlock() {
        super(BlockProperty.FOOD_ON_MEDIUM_CONTAINER);
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ObtainInteraction obtain = new ObtainInteraction(
                new ItemStack(IMItems.STEAMED_PUMPKIN_SLICE_WITH_CHOPPED_GARLIC.get()),
                ItemMatcher.empty(),
                ObtainInteraction.DropForm.INVENTORY,
                ObtainInteraction.ObtainingMethod.SERVE
        );
        return Either.left(obtain);
    }

    @Override
    public int getMaxInteraction() {
        return 5;
    }

    public static final VoxelShape SHAPE = Shapes.or(WoodenPlateBlock.SHAPE, Block.box(4, 1 , 4, 12, 3.5, 12));
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

}
