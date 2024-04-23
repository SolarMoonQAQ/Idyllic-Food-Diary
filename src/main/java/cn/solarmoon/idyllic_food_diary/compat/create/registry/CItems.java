package cn.solarmoon.idyllic_food_diary.compat.create.registry;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.compat.create.Create;
import cn.solarmoon.idyllic_food_diary.util.useful_data.FoodProperty;
import cn.solarmoon.solarmoon_core.api.common.item.SimpleFoodBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "create", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CItems {

    public static final DeferredRegister<Item> C_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, new Create().getModId());

    public static final RegistryObject<SimpleFoodBlockItem> WHEAT_DOUGH = C_ITEMS.register("dough",
            () -> new SimpleFoodBlockItem(IMBlocks.WHEAT_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT));


}
