package cn.solarmoon.idyllic_food_diary.compat.appleskin.event;

import cn.solarmoon.idyllic_food_diary.common.item.base.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.common.item.food.containable.SoupBowlFoodItem;
import cn.solarmoon.idyllic_food_diary.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.solarmoon_core.api.data.serializable.FoodValue;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

/**
 * 苹果皮联动：根据杯内液体显示对应食物属性、显示碗装汤类食物属性
 */
public class ShowCupFoodValueEvent {

    @SubscribeEvent
    public void onFoodValuesEvent(FoodValuesEvent event) {
        if (event.itemStack.getItem() instanceof AbstractCupItem) {
            ItemStack stack = event.itemStack;
            IFluidHandlerItem tankStack = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
            FluidEffect fluidEffect = FluidEffect.get(tankStack.getFluidInTank(0).getFluid().getFluidType().toString());
            if (fluidEffect == null) return;
            FoodValue foodValue = fluidEffect.getFoodValue();
            event.modifiedFoodValues = new FoodValues(foodValue.nutrition, foodValue.saturation);
        } else if (event.itemStack.getItem() instanceof SoupBowlFoodItem bowlFood) {
            FluidEffect fluidEffect = FluidEffect.get(bowlFood.fluidBound);
            if (fluidEffect == null) return;
            FoodValue foodValue = fluidEffect.getFoodValue();
            event.modifiedFoodValues = new FoodValues(foodValue.nutrition, foodValue.saturation);
        }
    }

}
