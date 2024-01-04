package cn.solarmoon.immersive_delight.compat.farmersdelight;

import cn.solarmoon.immersive_delight.compat.farmersdelight.items.WheatDoughItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;

/**
 * 农夫乐事
 */
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight {

    public static final DeferredRegister<Item> FD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, vectorwing.farmersdelight.FarmersDelight.MODID);

    public static final RegistryObject<WheatDoughItem> WHEAT_DOUGH_ITEM = FD_ITEMS.register("wheat_dough", WheatDoughItem::new);

    public static void register(IEventBus bus) {
        FD_ITEMS.register(bus);
    }

}
