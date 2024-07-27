package cn.solarmoon.idyllic_food_diary.registry.ability;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class IMComposterThs {

    public static void addRegistry() {
        put(IMItems.APPLE_CORE.get(), 0.3F);
        put(IMItems.DURIAN_CORE.get(), 0.3F);
        put(IMItems.CHOPPED_SPRING_ONION.get(), 0.3F);
        put(IMItems.CHOPPED_GINGER.get(), 0.3F);
        put(IMItems.SHREDDED_GINGER.get(), 0.3F);
        put(IMItems.CHOPPED_GARLIC.get(), 0.3F);
        put(IMItems.CHOPPED_SPRING_ONION.get(), 0.3F);
        put(IMItems.APPLE_SAPLING.get(), 0.3F);
        put(IMItems.DURIAN_SAPLING.get(), 0.3F);
        put(IMItems.GREEN_TEA_SEEDS.get(), 0.3F);
        put(IMItems.CHOPPED_SPRING_ONION.get(), 0.3F);

        put(IMItems.GINGER_SLICE.get(), 0.5F);
        put(IMItems.PUMPKIN_SLICE.get(), 0.5F);
        put(IMItems.SOYBEANS.get(), 0.5F);
        put(IMItems.SPRING_ONION.get(), 0.5F);
        put(IMItems.GARLIC_SPROUTS.get(), 0.5F);

        put(IMItems.WILD_GARLIC.get(), 0.65F);
        put(IMItems.WILD_GINGER.get(), 0.65F);
        put(IMItems.WILD_SPRING_ONION.get(), 0.65F);
        put(IMItems.GINGER.get(), 0.65F);
        put(IMItems.GARLIC.get(), 0.65F);
        put(IMItems.DURIAN_FLESH.get(), 0.65F);
        put(IMItems.DURIAN_SHELL.get(), 0.65F);
        put(IMItems.BLACK_TEA_LEAF.get(), 0.65F);
        put(IMItems.GREEN_TEA_LEAF.get(), 0.65F);

        put(IMItems.DURIAN.get(), 0.85F);
        put(IMItems.WHEAT_DOUGH.get(), 0.85F);
        put(IMItems.FLATBREAD_DOUGH.get(), 0.85F);
        put(IMItems.SHORTENING_DOUGH.get(), 0.85F);
        put(IMItems.PIE_CRUST.get(), 0.85F);
        put(IMItems.BAI_JI_BUN.get(), 0.85F);

        put(IMItems.ROASTED_DURIAN.get(), 1F);
        put(IMItems.ROASTED_BAI_JI_BUN.get(), 1F);
        put(IMItems.ROASTED_NAAN.get(), 1F);
        put(IMItems.STEAMED_BUN.get(), 1F);
        put(IMItems.CUSTARD_TART.get(), 1F);
        put(IMItems.STEAMED_PUMPKIN_SLICE_WITH_CHOPPED_GARLIC.get(), 1F);
    }

    private static void put(ItemLike item, float chance) {
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }

    public static void onFMLDefferSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(IMComposterThs::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(IMComposterThs::onFMLDefferSetup);
    }

}
