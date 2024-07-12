package cn.solarmoon.idyllic_food_diary.element.matter.stove;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.util.VoxelShapeUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.ILitBlock;
import cn.solarmoon.solarmoon_core.api.renderer.IFreeRenderBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 接入后，可以超级变换形态，也就是物品对着炉灶右键可以嵌入
 */
public interface IBuiltInStove extends IFreeRenderBlock, ILitBlock {

    BooleanProperty NESTED_IN_STOVE = BooleanProperty.create("nested_in_stove");

    default VoxelShape getShape(BlockState state) {
        Direction direction = state.getValue(IHorizontalFacingBlock.FACING);
        VoxelShape i = Shapes.block();
        VoxelShape ii = Block.box(5, 2, 0, 11, 7, 12);
        VoxelShape iii = Block.box(2, 10, 2, 14, 16, 14);
        VoxelShape result = Shapes.joinUnoptimized(i, Shapes.or(ii, iii), BooleanOp.ONLY_FIRST);
        return VoxelShapeUtil.rotateShape(direction, result);
    }

    /**
     * 用于renderer，将方块上移至炉灶槽位的高度
     */
    default void translateContent(Direction direction, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                IMBlocks.STOVE.get().defaultBlockState().setValue(IHorizontalFacingBlock.FACING, direction),
                poseStack, buffer, light, overlay);
        poseStack.translate(0, getYOffset(), 0);
    }

    default double getYOffset(BlockState state) {
        return state.getBlock() instanceof IBuiltInStove bis && bis.isNestedInStove(state) ? getYOffset() : 0;
    }

    default double getYOffset() {
        return 10 / 16f;
    }

    default boolean isNestedInStove(BlockState state) {
        return state.getValue(NESTED_IN_STOVE);
    }

}
