package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class TextUtil {

    public static String toRoman(int num) {
        String[] m = {"", "M", "MM", "MMM"};
        String[] c = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] x = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] i = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        String thousands = m[num/1000];
        String hundreds = c[(num%1000)/100];
        String tens = x[(num%100)/10];
        String ones = i[num%10];

        return thousands + hundreds + tens + ones;
    }

    public static Component translation(String string1, String string2, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects);
    }

    public static Component translation(String string1, String string2, ChatFormatting format, Object... objects) {
        return Component.translatable(string1 + "." + ImmersiveDelight.MOD_ID + "." + string2, objects)
                .withStyle(format);
    }

}
