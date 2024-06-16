package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.capability.FoodItemData;
import cn.solarmoon.idyllic_food_diary.feature.capability.IFoodItemData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class IMCapabilities {

    public static final Capability<IFoodItemData> FOOD_ITEM_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public void onAttachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        event.addCapability(id("food_item_data"), new FoodItemData(event.getObject()));
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IFoodItemData.class);
    }

    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onFMLCommonSetup);
    }

    private ResourceLocation id(String id) {
        return new ResourceLocation(IdyllicFoodDiary.MOD_ID, id);
    }

}
