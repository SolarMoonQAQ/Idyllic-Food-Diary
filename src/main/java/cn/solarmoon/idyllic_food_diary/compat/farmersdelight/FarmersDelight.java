package cn.solarmoon.idyllic_food_diary.compat.farmersdelight;

import cn.solarmoon.solarmoon_core.api.ObjectRegistry;
import net.minecraftforge.fml.ModList;

/**
 * 农夫乐事
 */
public class FarmersDelight {

    public static final String MOD_ID = "farmersdelight";
    public static final ObjectRegistry REGISTRY = ObjectRegistry.create(MOD_ID, false);

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void register() {
        if (isLoaded()) {
            FDItems.register();
        }
    }

}
