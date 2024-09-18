package cn.solarmoon.idyllic_food_diary;

import cn.solarmoon.idyllic_food_diary.data.DataGenerater;
import cn.solarmoon.idyllic_food_diary.registry.client.*;
import cn.solarmoon.idyllic_food_diary.registry.common.*;
import cn.solarmoon.spark_core.api.entry_builder.ObjectRegister;
import cn.solarmoon.spark_core.api.kit.Translator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mod(IdyllicFoodDiary.MOD_ID)
public class IdyllicFoodDiary {

    public static final String MOD_ID = "idyllic_food_diary";
    public static final Logger LOGGER = LoggerFactory.getLogger("田园食记");
    public static final Translator TRANSLATOR = new Translator(MOD_ID);
    public static final ObjectRegister REGISTER = new ObjectRegister(MOD_ID, true);

    public IdyllicFoodDiary(IEventBus modEventBus, ModContainer modContainer) {
        REGISTER.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            IFDBlockEntityRenderers.register(modEventBus);
            IFDItemRenderers.register(modEventBus);
            IFDGuis.register(modEventBus);
            IFDBakedModelOverrides.register(modEventBus);
            IFDTooltips.register(modEventBus);
            IFDParticleProviders.register(modEventBus);
            IFDLayers.register();
            IFDClientEvents.register();
        }

        IFDItems.register();
        IFDBlocks.register();
        IFDBlockEntities.register();
        IFDDataComponents.register();
        IFDAttachments.register();
        IFDRecipes.register();
        IFDCreativeTabs.register();
        IFDSounds.register();
        IFDNetPacks.register();
        IFDDatas.register();
        IFDParticles.register();

        DataGenerater.register(modEventBus);

    }


}