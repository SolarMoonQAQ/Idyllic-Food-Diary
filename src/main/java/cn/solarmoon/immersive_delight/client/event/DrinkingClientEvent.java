package cn.solarmoon.immersive_delight.client.event;

import cn.solarmoon.immersive_delight.common.item.base.AbstractCupItem;
import cn.solarmoon.immersive_delight.common.item.base.AbstractKettleItem;
import cn.solarmoon.immersive_delight.common.item.block_item.SoupPotItem;
import cn.solarmoon.immersive_delight.common.registry.IMPacks;
import cn.solarmoon.immersive_delight.util.namespace.NETList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.FluidPouring.fluidPouring;

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
        if(stack.getItem() instanceof AbstractCupItem || stack.getItem() instanceof AbstractKettleItem || stack.getItem() instanceof SoupPotItem) {
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
