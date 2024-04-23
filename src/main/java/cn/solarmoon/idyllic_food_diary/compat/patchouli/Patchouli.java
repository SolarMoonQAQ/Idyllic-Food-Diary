package cn.solarmoon.idyllic_food_diary.compat.patchouli;

import net.minecraftforge.fml.ModList;

public class Patchouli {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("patchouli");
    }

}
