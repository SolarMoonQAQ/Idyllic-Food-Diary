package cn.solarmoon.idyllic_food_diary.registry.ability;

import cn.solarmoon.idyllic_food_diary.feature.spit_item.SpittableItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.entry.common.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class IMSpittableItems {

    public static void addRegistry() {
        put(Items.APPLE, new ItemStack(IMItems.APPLE_CORE.get()));
        put(IMItems.DURIAN_FLESH, new ItemStack(IMItems.DURIAN_CORE.get()));
        put(IMItems.ROASTED_SUCKLING_PIG_HEAD, new ItemStack(Items.BONE));
    }

    public static void put(Item itemBound, ItemStack spit) {
        SpittableItem.ALL.add(new SpittableItem(itemBound, spit));
    }

    public static void put(ItemEntry<?> itemBound, ItemStack spit) {
        if (itemBound.isPresent()) {
            SpittableItem.ALL.add(new SpittableItem(itemBound.get(), spit));
        }
    }

    public static void onFMLDefferSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(IMSpittableItems::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(IMSpittableItems::onFMLDefferSetup);
    }

}
