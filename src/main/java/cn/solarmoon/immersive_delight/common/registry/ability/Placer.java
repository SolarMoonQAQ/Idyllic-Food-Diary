package cn.solarmoon.immersive_delight.common.registry.ability;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.registry.ability.CustomPlaceableItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class Placer {

    public static void register() {
        CustomPlaceableItem.put(Items.MUSHROOM_STEW, IMBlocks.MUSHROOM_STEW.get());
        CustomPlaceableItem.put(Items.BEETROOT_SOUP, IMBlocks.BEETROOT_SOUP.get());
        CustomPlaceableItem.put(Items.BOWL, IMBlocks.BOWL.get());
    }

    public static void onFMLSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(Placer::register);
    }

}
