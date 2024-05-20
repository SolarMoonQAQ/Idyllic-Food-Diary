package cn.solarmoon.idyllic_food_diary.api.common.item;

import cn.solarmoon.idyllic_food_diary.api.common.block_entity.ISpiceable;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.solarmoon_core.api.common.item.IContainerItem;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractSpiceJarItem extends BlockItem implements IContainerItem {

    public AbstractSpiceJarItem(Block block) {
        super(block, new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ISpiceable sp && hasEnoughSpice(stack) && !sp.timeToResetSpices()) {
            ItemStackHandler inv = ContainerUtil.getInventory(stack);
            sp.addSpice(new Spice(inv.extractItem(0, 1, false).getItem(), sp.getSpiceStep()));
            ContainerUtil.setInventory(stack, inv);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    public static boolean hasEnoughSpice(ItemStack stack) {
        return !ContainerUtil.getInventory(stack).getStackInSlot(0).isEmpty();
    }

}
