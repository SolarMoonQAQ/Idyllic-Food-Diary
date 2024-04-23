package cn.solarmoon.idyllic_food_diary.common.registry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.solarmoon_core.api.registry.IRegister;
import cn.solarmoon.solarmoon_core.api.registry.object.CreativeTabEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public enum IMCreativeModeTab implements IRegister {
    INSTANCE;

    public static final CreativeTabEntry CREATIVE_TAB = IdyllicFoodDiary.REGISTRY.creativeTab()
            .id(IdyllicFoodDiary.MOD_ID)
            .builder(CreativeModeTab.builder()
                            .title(IdyllicFoodDiary.TRANSLATOR.set("creative_mode_tab", "main"))
                            .icon(() -> new ItemStack(IMItems.ROLLING_PIN.get()))
                            .displayItems((params, output) ->
                                    IdyllicFoodDiary.REGISTRY.itemRegister.getEntries().stream()
                                            .map(RegistryObject::get)
                                            .forEach(output::accept))
                    )
            .build();

}