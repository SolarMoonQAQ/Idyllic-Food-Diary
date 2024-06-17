package cn.solarmoon.idyllic_food_diary.compat.appleskin;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaIngredientXX;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
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
            List<TeaIngredientXX> teaIngredients = TeaIngredientXX.readFromFluidStack(fluidStack);
            int n = 0;
            float s = 0;
            for (var ti : teaIngredients) {
                n = n + ti.getFoodValue().nutrition;
                s = s + ti.getFoodValue().saturation;
            }
            if (n > 0 || s > 0) event.modifiedFoodValues = new FoodValues(n, s);
        }
    }

}
