package cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ConsumeInteraction;
import cn.solarmoon.idyllic_food_diary.element.matter.food.interactive_food_block.food_interaction.ObtainInteraction;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.idyllic_food_diary.util.useful_data.BlockProperty;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.util.device.ItemMatcher;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoastedSucklingPigBlock extends AbstractInteractiveFoodBlock implements IBedPartBlock {

    public RoastedSucklingPigBlock() {
        super(BlockProperty.FOOD_ON_LARGE_CONTAINER);
    }

    @Override
    public int getMaxInteraction() {
        return 5;
    }

    @Override
    public Either<ObtainInteraction, ConsumeInteraction> getSpecialInteraction(int stageIndex) {
        ObtainInteraction obtain = new ObtainInteraction(
                new ItemStack(IMItems.ROASTED_SUCKLING_PORK.get()),
                ItemMatcher.of(IMItemTags.FORGE_KNIVES),
                ObtainInteraction.DropForm.INVENTORY,
                ObtainInteraction.ObtainingMethod.SPLIT
        );
        if (stageIndex == 1) return Either.left(
                obtain.copy()
                .setMatcher(ItemMatcher.any())
                .setDrop(new ItemStack(Items.BONE, 2))
                .setDropForm(ObtainInteraction.DropForm.DROP)
        );
        return Either.left(obtain);
    }

    public static VoxelShape SHAPE(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == BedPart.HEAD ?
                VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 0, 1, 15, 8, 16))
                : VoxelShapeUtil.rotateShape(state.getValue(FACING), Block.box(1, 0, 0, 15, 8, 15));
    };
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE(state, getter, pos, context);
    }

}
