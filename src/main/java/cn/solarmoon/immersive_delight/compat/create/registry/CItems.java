package cn.solarmoon.immersive_delight.compat.create.registry;

import cn.solarmoon.immersive_delight.common.item.food_block_item.WheatDoughItem;
import cn.solarmoon.immersive_delight.compat.create.Create;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "create", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CItems {

    public static final DeferredRegister<Item> C_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, new Create().getModId());

    public static final RegistryObject<WheatDoughItem> WHEAT_DOUGH = C_ITEMS.register("dough", WheatDoughItem::new);


}
