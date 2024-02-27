package cn.solarmoon.immersive_delight;

import cn.solarmoon.immersive_delight.client.registry.*;
import cn.solarmoon.immersive_delight.common.registry.*;
import cn.solarmoon.immersive_delight.compat.appleskin.AppleSkin;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.common.registry.Config;
import cn.solarmoon.solarmoon_core.registry.core.ObjectRegistry;
import cn.solarmoon.solarmoon_core.util.static_utor.Debug;
import cn.solarmoon.solarmoon_core.util.static_utor.Translator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.solarmoon.immersive_delight.ImmersiveDelight.MOD_ID;


@Mod(MOD_ID)
public class ImmersiveDelight {

    public static final String MOD_ID = "immersive_delight";
    public static final Logger LOGGER = LoggerFactory.getLogger("Immersive Delight");
    public static Debug DEBUG = new Debug("[§6沉浸乐事§f] ", Config.deBug);
    public static final Translator TRANSLATOR = new Translator(MOD_ID);
    public static final ObjectRegistry REGISTRY = new ObjectRegistry(MOD_ID).create();


    public ImmersiveDelight() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //物品
        IMItems.INSTANCE.register();
        //方块
        IMBlocks.INSTANCE.register();
        //方块实体
        IMBlockEntities.INSTANCE.register();
        //流体
        IMFluids.INSTANCE.register();
        //药水效果
        IMEffects.INSTANCE.register();
        //TBA栏
        IMCreativeModeTab.INSTANCE.register();
        //配方
        IMRecipes.INSTANCE.register();
        //粒子
        IMParticles.INSTANCE.register();
        //音效
        IMSounds.INSTANCE.register();
        //生成
        IMFeatures.INSTANCE.register();
        //生物
        IMEntityTypes.INSTANCE.register();
        //网络包
        IMPacks.INSTANCE.register();
        //渲染层
        IMLayers.INSTANCE.register();

        //客户端事件
        new IMClientEvents().register(bus);
        //双端事件
        new IMCommonEvents().register(bus);
        //方块实体渲染
        new IMBlockEntityRenderers().register(bus);
        //实体渲染
        new IMEntityRenderers().register(bus);
        //gui
        new IMGui().register(bus);
        //tooltip
        new IMTooltipRenderers().register(bus);
        //数据包
        new IMDataPacks().register();

        //配置文件
        new Config().register();

        bus.addListener(Composter::onFMLSetup);

        //—————————————————————————联动—————————————————————————//
        new FarmersDelight().register(bus);
        new AppleSkin().register(bus);
        new Create().register(bus);
    }

}