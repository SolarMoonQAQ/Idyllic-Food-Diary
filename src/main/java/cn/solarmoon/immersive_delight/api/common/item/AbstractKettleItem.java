package cn.solarmoon.immersive_delight.api.common.item;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;

public abstract class AbstractKettleItem extends BaseTankItem {

    public AbstractKettleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        Direction direction = player.getDirection();
        FluidActionResult result = FluidUtil.tryPickUpFluid(heldStack, player, level, null, direction);
        if (result.isSuccess()) return InteractionResultHolder.success(heldStack);
        return InteractionResultHolder.fail(heldStack);
    }

}
