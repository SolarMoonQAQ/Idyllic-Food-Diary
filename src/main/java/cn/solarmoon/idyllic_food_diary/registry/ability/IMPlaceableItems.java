package cn.solarmoon.idyllic_food_diary.registry.ability;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.common.ability.CustomPlaceableItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class IMPlaceableItems {

    private static void addRegistry() {
        put(Items.MUSHROOM_STEW, IMBlocks.MUSHROOM_STEW.get());
        put(Items.BEETROOT_SOUP, IMBlocks.BEETROOT_SOUP.get());
        put(Items.BOWL, IMBlocks.BOWL.get());
    }

    private static void put(Item item, Block block) {
        CustomPlaceableItem.put(item, block);
    }

    private static void onFMLDefferSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(IMPlaceableItems::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(IMPlaceableItems::onFMLDefferSetup);
    }

}
