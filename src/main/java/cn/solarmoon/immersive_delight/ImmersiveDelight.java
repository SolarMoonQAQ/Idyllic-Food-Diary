package cn.solarmoon.immersive_delight;

import cn.solarmoon.immersive_delight.client.IMItemRenderers;
import cn.solarmoon.immersive_delight.common.IMFluids;
import cn.solarmoon.immersive_delight.compat.apple_skin.AppleSkin;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.data.fluid_effects.BuilderFluidEffects;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.util.Util;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;
import static cn.solarmoon.immersive_delight.init.CreativeModeTab.CREATIVE_TAB;
import static cn.solarmoon.immersive_delight.init.ObjectRegister.*;


@Mod(MOD_ID)
public class ImmersiveDelight {

    public static final String MOD_ID = "immersive_delight";

    public static final Logger LOGGER = LoggerFactory.getLogger("Immersive Delight");

    public ImmersiveDelight() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        //物品
        ITEMS.register(bus);

        //方块
        BLOCKS.register(bus);

        //方块实体
        BLOCK_ENTITIES.register(bus);

        //流体
        FLUIDS.register(bus);
        FLUID_TYPES.register(bus);

        //药水效果
        EFFECTS.register(bus);

        //TBA栏
        CREATIVE_TAB.register(bus);

        //配方
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);

        //粒子
        PARTICLE_TYPES.register(bus);

        //音效
        SOUNDS.register(bus);

        //配置文件
        Config.register();

        MinecraftForge.EVENT_BUS.register(this);

        //这个静动态混了所以主线也注册
        MinecraftForge.EVENT_BUS.register(new IMItemRenderers());

        //—————————————————————————联动—————————————————————————//
        if(Util.isLoad(vectorwing.farmersdelight.FarmersDelight.MODID)) {
            FarmersDelight.register(bus);
        }
        if(Util.isLoad("appleskin")) {
            MinecraftForge.EVENT_BUS.register(new AppleSkin());
        }

    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(BuilderFluidEffects.INSTANCE);
    }

}