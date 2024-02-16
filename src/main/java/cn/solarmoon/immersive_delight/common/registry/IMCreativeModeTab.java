package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.CreativeTabEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public enum IMCreativeModeTab implements IRegister {
    INSTANCE;

    public static final CreativeTabEntry CREATIVE_TAB = ImmersiveDelight.REGISTRY.creativeTabEntry()
            .id(ImmersiveDelight.MOD_ID)
            .builder(CreativeModeTab.builder()
                            .title(ImmersiveDelight.TRANSLATOR.set("creative_mode_tab", "main"))
                            .icon(() -> new ItemStack(IMItems.ROLLING_PIN.get()))
                            .displayItems((params, output) ->
                                    ImmersiveDelight.REGISTRY.itemRegister.getEntries().stream()
                                            .map(RegistryObject::get)
                                            .forEach(output::accept))
                    )
            .build();

}