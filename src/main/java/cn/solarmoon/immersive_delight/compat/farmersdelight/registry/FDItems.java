package cn.solarmoon.immersive_delight.compat.farmersdelight.registry;

import cn.solarmoon.immersive_delight.common.items.WheatDoughItem;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "farmersdelight", bus = Mod.EventBusSubscriber.Bus.MOD)
public class FDItems {

    public static final DeferredRegister<Item> FD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, new FarmersDelight().getModId());

    /**
     * 根据农夫乐事的加载与否决定是否覆盖fd面团
     */
    public static final RegistryObject<WheatDoughItem> WHEAT_DOUGH = FD_ITEMS.register("wheat_dough", WheatDoughItem::new);


}
