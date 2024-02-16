package cn.solarmoon.immersive_delight.compat.appleskin.event;

import cn.solarmoon.immersive_delight.common.item.base.AbstractCupItem;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FoodValue;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

/**
 * 苹果皮联动：根据杯内液体显示对应食物属性
 */
public class ShowCupFoodValueEvent {

    @SubscribeEvent
    public void onFoodValuesEvent(FoodValuesEvent event) {
        if(event.itemStack.getItem() instanceof AbstractCupItem) {
            ItemStack stack = event.itemStack;
            IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
            FluidEffect fluidEffect = FluidEffect.get(tankStack.getFluidInTank(0).getFluid().getFluidType().toString());
            if(fluidEffect == null) return;
            FoodValue foodValue = fluidEffect.getFoodValue();
            event.modifiedFoodValues = new FoodValues(foodValue.hunger, foodValue.saturation);
        }
    }

}
