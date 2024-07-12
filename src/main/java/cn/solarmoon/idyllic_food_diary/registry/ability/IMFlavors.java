package cn.solarmoon.idyllic_food_diary.registry.ability;

import cn.solarmoon.idyllic_food_diary.feature.spice.Flavor;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class IMFlavors {

    public static void addRegistry() {
        Flavor.put(IMItems.SALT.get(), List.of(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0)), Flavor.DEFAULT_LETHAL_DOSE);
        Flavor.put(Items.SUGAR, List.of(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0)), Flavor.DEFAULT_LETHAL_DOSE);
    }

    public static void onFMLDefferSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(IMFlavors::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(IMFlavors::onFMLDefferSetup);
    }

}
