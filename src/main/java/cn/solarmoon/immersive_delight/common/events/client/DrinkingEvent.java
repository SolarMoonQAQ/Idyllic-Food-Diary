package cn.solarmoon.immersive_delight.common.events.client;

import cn.solarmoon.immersive_delight.common.items.abstract_items.DrinkableItem;
import cn.solarmoon.immersive_delight.common.items.abstract_items.WaterKettleItem;
import cn.solarmoon.immersive_delight.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.FluidPouring.fluidPouring;

/**
 * shift+左键把水倒空
 */
public class DrinkingEvent {

    @SubscribeEvent
    public void pour(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        if(stack.getItem() instanceof DrinkableItem || stack.getItem() instanceof WaterKettleItem) {
            if(event.getEntity().isCrouching()) {
                ServerPackSerializer.sendPacket(pos, Blocks.AIR, stack, "pouring");
                IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
                BlockState fluidState = tankStack.getFluidInTank(0).getFluid().defaultFluidState().createLegacyBlock();
                FluidStack fluidStack = tankStack.getFluidInTank(0);
                int tankAmount = fluidStack.getAmount();
                fluidPouring(fluidState, tankAmount);
            }
        }
    }

}
