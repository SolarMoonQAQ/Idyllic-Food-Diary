package cn.solarmoon.idyllic_food_diary.compat.patchouli;

import net.minecraftforge.fml.ModList;

public class Patchouli {

    public static final String MOD_ID = "patchouli";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void register() {
        if (isLoaded()) {
            PItems.register();
        }
    }

}
