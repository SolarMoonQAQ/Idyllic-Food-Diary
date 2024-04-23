package cn.solarmoon.idyllic_food_diary.client.event;

import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractKettleItem;
import cn.solarmoon.idyllic_food_diary.common.item.block_item.SoupPotItem;
import cn.solarmoon.idyllic_food_diary.common.item.block_item.SteamerBaseItem;
import cn.solarmoon.idyllic_food_diary.common.registry.IMPacks;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import static cn.solarmoon.idyllic_food_diary.client.particle.vanilla.FluidPouring.fluidPouring;

/**
 * shift+左键把水倒空
 */
public class DrinkingClientEvent {

    /**
     * 可以把水泼出去并对范围内的实体产生效果
     */
    @SubscribeEvent
    public void pour(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        if(stack.getItem() instanceof AbstractCupItem
                || stack.getItem() instanceof AbstractKettleItem
                || stack.getItem() instanceof SoupPotItem
                || stack.getItem() instanceof SteamerBaseItem
        ) {
            if(event.getEntity().isCrouching()) {
                IMPacks.SERVER_PACK.getSender().send(NETList.POURING, pos);
                IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
                FluidStack fluidStack = tankStack.getFluidInTank(0);
                int tankAmount = fluidStack.getAmount();
                fluidPouring(fluidStack, tankAmount);
            }
        }
    }

}
