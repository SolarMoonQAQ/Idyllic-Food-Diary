package cn.solarmoon.idyllic_food_diary.compat.appleskin.client.event;

import cn.solarmoon.idyllic_food_diary.api.common.item.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.api.common.item.containable.SoupBowlFoodItem;
import cn.solarmoon.idyllic_food_diary.api.data.serializer.FluidEffect;
import cn.solarmoon.idyllic_food_diary.api.data.serializer.TeaIngredient;
import cn.solarmoon.solarmoon_core.api.data.serializable.FoodValue;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

import java.util.List;

/**
 * 苹果皮联动：根据杯内液体显示对应食物属性、显示碗装汤类食物属性
 */
public class ShowCupFoodValueEvent {

    @SubscribeEvent
    public void onFoodValuesEvent(FoodValuesEvent event) {
        if (event.itemStack.getItem() instanceof AbstractCupItem) {
            ItemStack stack = event.itemStack;
            FluidStack fluidStack = FluidUtil.getFluidStack(stack);
            List<TeaIngredient> teaIngredients = TeaIngredient.readFromFluidStack(fluidStack);
            if (teaIngredients != null) {
                int n = 0;
                float s = 0;
                for (var ti : teaIngredients) {
                    n = n + ti.getFoodValue().nutrition;
                    s = s + ti.getFoodValue().saturation;
                }
                if (n > 0 || s > 0) event.modifiedFoodValues = new FoodValues(n, s);
            }
        } else if (event.itemStack.getItem() instanceof SoupBowlFoodItem bowlFood) {
            FluidEffect fluidEffect = FluidEffect.get(bowlFood.fluidBound);
            if (fluidEffect == null) return;
            FoodValue foodValue = fluidEffect.getFoodValue();
            event.modifiedFoodValues = new FoodValues(foodValue.nutrition, foodValue.saturation);
        }
    }

}
