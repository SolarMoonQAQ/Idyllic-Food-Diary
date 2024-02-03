package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.api.registry.BaseObjectRegistry;
import cn.solarmoon.immersive_delight.util.CoreUtil;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static cn.solarmoon.immersive_delight.common.registry.IMItems.ROLLING_PIN;
import static cn.solarmoon.immersive_delight.common.registry.IMItems.ITEMS;

public class IMCreativeModeTab extends BaseObjectRegistry<CreativeModeTab> {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, ImmersiveDelight.MOD_ID);

    public IMCreativeModeTab() {
        super(CREATIVE_TAB);
    }

    public static final RegistryObject<CreativeModeTab> IMMERSIVE_DELIGHT_TAB = CREATIVE_TAB.register("immersive_delight", () -> net.minecraft.world.item.CreativeModeTab.builder()
            .title(CoreUtil.translation("creative_mode_tab", "main"))
            .icon(() -> new ItemStack(ROLLING_PIN.get()))
            .displayItems((params, output) ->
                    ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .forEach(output::accept))
            .build()
    );

}