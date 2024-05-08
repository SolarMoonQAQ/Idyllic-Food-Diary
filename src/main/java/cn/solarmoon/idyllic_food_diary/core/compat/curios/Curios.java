package cn.solarmoon.idyllic_food_diary.core.compat.curios;

import net.minecraftforge.fml.ModList;

public class Curios {

    public static final String MOD_ID = "curios";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void register() {
        if (isLoaded()) {
        }
    }

}
