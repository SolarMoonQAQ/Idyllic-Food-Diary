package cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 使得炉灶可以方便调用插入的方块实体的方块的use方法而不会报错
 */
public interface IInlineBlockMethodCall {

    InteractionResult doUse(Level level, BlockPos pos, Player player, InteractionHand hand, BlockEntity blockEntity);

}
