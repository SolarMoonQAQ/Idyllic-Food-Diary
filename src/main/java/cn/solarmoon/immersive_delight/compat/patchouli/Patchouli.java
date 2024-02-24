package cn.solarmoon.immersive_delight.compat.patchouli;

import net.minecraftforge.fml.ModList;

public class Patchouli {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("patchouli");
    }

}
