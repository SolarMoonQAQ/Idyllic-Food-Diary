package cn.solarmoon.idyllic_food_diary.core.compat.patchouli;

import cn.solarmoon.idyllic_food_diary.core.compat.patchouli.common.registry.PItems;
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
