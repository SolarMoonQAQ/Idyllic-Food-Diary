package cn.solarmoon.idyllic_food_diary.api.common.block.cookware;

import cn.solarmoon.idyllic_food_diary.core.common.block_entity.SpiceJarBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block.IBlockUseCaller;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.Event;

public abstract class AbstractSpiceJarEntityBlock extends BasicEntityBlock implements IBlockUseCaller {

    public AbstractSpiceJarEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        SpiceJarBlockEntity jar = (SpiceJarBlockEntity) level.getBlockEntity(pos);
        if (jar == null) return InteractionResult.PASS;

        if (getThis(player, level, pos, state, hand, true)) {
            return InteractionResult.SUCCESS;
        }

        if (jar.specialStorage(player, hand)) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public Event.Result getUseResult(BlockState blockState, BlockPos blockPos, Level level, Player player, ItemStack itemStack, BlockHitResult blockHitResult, InteractionHand hand) {
        return Event.Result.ALLOW;
    }

}
