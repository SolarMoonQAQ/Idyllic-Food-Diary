package cn.solarmoon.immersive_delight.common.registry;

import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 堆肥
 */
public class Composter {

    public static void put() {
        ComposterBlock.COMPOSTABLES.put(IMItems.APPLE_CORE.get(), 0.3F);
    }

    public static void onFMLSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Composter::put);
    }

}
