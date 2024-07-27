package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TooltipUtil {

    @FunctionalInterface
    public interface TooltipAdder {
        void add();
    }

    public static void addShiftShowTooltip(List<Component> tooltips, TooltipAdder adder) {
        if (Screen.hasShiftDown()) {
            tooltips.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "shift_on"));
            adder.add();
        } else {
            tooltips.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "shift_off"));
        }
    }

}
