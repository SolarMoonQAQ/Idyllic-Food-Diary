package cn.solarmoon.immersive_delight.compat.farmersdelight;

import cn.solarmoon.immersive_delight.common.items.WheatDoughItem;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.common.IMBlocks.WHEAT_DOUGH_ID;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

/**
 * 农夫乐事
 */
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FarmersDelight {

    public static final DeferredRegister<Item> FD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, vectorwing.farmersdelight.FarmersDelight.MODID);

    /**
     * 根据农夫乐事的加载与否决定是覆盖农夫乐事还是注册自己的
     */
    public static final RegistryObject<WheatDoughItem> WHEAT_DOUGH = (Util.isLoad(vectorwing.farmersdelight.FarmersDelight.MODID)
            ? FD_ITEMS : ITEMS)
            .register(WHEAT_DOUGH_ID,WheatDoughItem::new);

    public static void register(IEventBus bus) {
        FD_ITEMS.register(bus);
    }

}
