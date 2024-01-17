package cn.solarmoon.immersive_delight.init;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.util.TextUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.ITEMS;

public class CreativeModeTab {

    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, ImmersiveDelight.MOD_ID);

    public static final RegistryObject<net.minecraft.world.item.CreativeModeTab> IMMERSIVE_DELIGHT_TAB = CREATIVE_TAB.register("immersive_delight", () -> net.minecraft.world.item.CreativeModeTab.builder()
            .title(TextUtil.translation("creative_mode_tab", "main"))
            .icon(() -> new ItemStack(ROLLING_PIN.get()))
            .displayItems((params, output) ->
                    ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .forEach(output::accept))
            .build()
    );

}