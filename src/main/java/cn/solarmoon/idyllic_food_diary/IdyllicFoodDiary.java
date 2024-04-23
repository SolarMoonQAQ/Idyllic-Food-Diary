package cn.solarmoon.idyllic_food_diary;

import cn.solarmoon.idyllic_food_diary.client.registry.*;
import cn.solarmoon.idyllic_food_diary.common.config.IMCommonConfig;
import cn.solarmoon.idyllic_food_diary.common.registry.*;
import cn.solarmoon.idyllic_food_diary.common.registry.ability.IMComposterThs;
import cn.solarmoon.idyllic_food_diary.common.registry.ability.IMPlaceableItems;
import cn.solarmoon.idyllic_food_diary.common.registry.ability.IMTickers;
import cn.solarmoon.idyllic_food_diary.common.registry.ability.IMTileDataHolders;
import cn.solarmoon.idyllic_food_diary.compat.appleskin.AppleSkin;
import cn.solarmoon.idyllic_food_diary.compat.create.Create;
import cn.solarmoon.idyllic_food_diary.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.solarmoon_core.api.SolarMoonBase;
import cn.solarmoon.solarmoon_core.api.registry.ObjectRegistry;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Debug;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Translator;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary.MOD_ID;


@Mod(MOD_ID)
public class IdyllicFoodDiary extends SolarMoonBase {

    public static final String MOD_ID = "idyllic_food_diary";
    public static Debug DEBUG = Debug.create("[§6田园食记§f] ", IMCommonConfig.deBug);
    public static final Translator TRANSLATOR = Translator.create(MOD_ID);
    public static final ObjectRegistry REGISTRY = ObjectRegistry.create(MOD_ID);

    @Override
    public void objects() {
        IMItems.INSTANCE.register();
        IMBlocks.INSTANCE.register();
        IMBlockEntities.INSTANCE.register();
        IMFluids.INSTANCE.register();
        IMEffects.INSTANCE.register();
        IMCreativeModeTab.INSTANCE.register();
        IMRecipes.INSTANCE.register();
        IMParticles.INSTANCE.register();
        IMFeatures.INSTANCE.register();
        IMEntityTypes.INSTANCE.register();
        IMPacks.INSTANCE.register();
        IMLayers.INSTANCE.register();
        IMSounds.INSTANCE.register();
    }

    @Override
    public void eventObjects() {
        new IMClientEvents().register();
        new IMCommonEvents().register();
        new IMGui().register();
        new IMTooltipRenderers().register();
        new IMDataPacks().register();
    }

    @Override
    public void xData() {
        IMCommonConfig.register();
    }

    @Override
    public void abilities() {
        IMTickers.INSTANCE.register();
        IMTileDataHolders.INSTANCE.register();
        new IMPlaceableItems().register();
        new IMComposterThs().register();
    }

    @Override
    public void compats() {
        new FarmersDelight().register();
        new AppleSkin().register();
        new Create().register();
    }

}