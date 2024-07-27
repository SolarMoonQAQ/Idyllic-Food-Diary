package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.feast;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate.PlateBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.AbstractInteractiveFoodBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.phys.VoxelShapeUtil;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.matcher.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BeggarsChickenBlock extends AbstractInteractiveFoodBlock {

    public BeggarsChickenBlock() {
        super(BlockProperty.FOOD_ON_MEDIUM_CONTAINER);
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ObtainInteraction obtain = new ObtainInteraction(
                new ItemStack(IMItems.BEGGARS_CHICKEN_MEAT.get()),
                ItemMatcher.of(IMItemTags.FORGE_KNIVES),
                ObtainInteraction.DropForm.INVENTORY,
                ObtainInteraction.ObtainingMethod.SPLIT
        );
        if (stageIndex == 5) return Either.left(new ObtainInteraction(
                ItemStack.EMPTY,
                ItemMatcher.any(),
                ObtainInteraction.DropForm.DROP,
                ObtainInteraction.ObtainingMethod.SPLIT,
                SoundEvents.EMPTY,
                true
        ));
        if (stageIndex == 1) return Either.left(
                obtain.copy()
                .setMatcher(ItemMatcher.any())
                .setDrop(new ItemStack(Items.BONE, 2))
                .setDropForm(ObtainInteraction.DropForm.DROP)
        );
        return Either.left(obtain);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        int interact = state.getValue(INTERACTION);
        if (interact == 5) return SoundType.STONE;
        return SoundType.AZALEA_LEAVES;
    }

    @Override
    public int getMaxInteraction() {
        return 5;
    }

    public static VoxelShape SHAPE(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.or(PlateBlock.SHAPE, Block.box(2, 1, 2, 14, 7, 14));
        if (state.getValues().get(INTERACTION) != null) {
            int interaction = state.getValue(INTERACTION);
            Direction facing = state.getValue(FACING).getOpposite();
            if (interaction == 4) {
                shape = Shapes.or(PlateBlock.SHAPE,
                        Block.box(2, 1, 2, 10, 7, 14),
                        Block.box(5, 1, 5, 11, 6, 11)
                );
            } else if (interaction == 3) {
                shape = Shapes.or(PlateBlock.SHAPE,
                        Block.box(2, 1, 2, 8, 7, 14),
                        Block.box(5, 1, 5, 11, 6, 11)
                );
            } else if (interaction == 2) {
                shape = Shapes.or(PlateBlock.SHAPE,
                        Block.box(2, 1, 4, 5, 7, 12),
                        Block.box(5, 1, 5, 11, 6, 11)
                );
            } else if (interaction == 1) {
                shape = Shapes.or(PlateBlock.SHAPE,
                        Block.box(5, 1, 5, 11, 6, 11)
                );
            }
            shape = VoxelShapeUtil.rotateShape(facing, shape);
        }
        return shape;
    }
    @Override
    public VoxelShape getOriginShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE(state, getter, pos, context);
    }
}
