package cn.solarmoon.immersive_delight;

import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.data.json_loader.FluidEffectLoader;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.CreativeModeTab.CREATIVE_TAB;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;


@Mod(MOD_ID)
public class ImmersiveDelight {
    public static final String MOD_ID = "immersive_delight";

    //总线
    public ImmersiveDelight() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        //物品
        ITEMS.register(bus);

        //方块
        BLOCKS.register(bus);

        //方块实体
        BLOCK_ENTITIES.register(bus);

        //TBA栏
        CREATIVE_TAB.register(bus);

        //配方
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);

        //粒子
        PARTICLE_TYPES.register(bus);

        //配置文件
        Config.register();

        MinecraftForge.EVENT_BUS.register(this);

        //———————————————————————联动—————————————————————————
        if(ModList.get().isLoaded(vectorwing.farmersdelight.FarmersDelight.MODID)) {
            FarmersDelight.register(bus);
        }

    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(FluidEffectLoader.INSTANCE);
    }

}