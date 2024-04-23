package cn.solarmoon.idyllic_food_diary.compat.farmersdelight.registry;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.idyllic_food_diary.util.useful_data.FoodProperty;
import cn.solarmoon.solarmoon_core.api.common.item.SimpleFoodBlockItem;
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
    public static final RegistryObject<SimpleFoodBlockItem> WHEAT_DOUGH = FD_ITEMS.register("wheat_dough",
            () -> new SimpleFoodBlockItem(IMBlocks.WHEAT_DOUGH.get(), FoodProperty.PRIMARY_HUNGER_PRODUCT));


}
