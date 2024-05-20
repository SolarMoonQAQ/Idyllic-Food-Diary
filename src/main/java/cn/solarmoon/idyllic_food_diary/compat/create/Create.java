package cn.solarmoon.idyllic_food_diary.compat.create;

import net.minecraftforge.fml.ModList;

/**
 * 机械动力
 */
public class Create {

    public static final String MOD_ID = "create";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void register() {
        if (isLoaded()) {
        }
    }

}
