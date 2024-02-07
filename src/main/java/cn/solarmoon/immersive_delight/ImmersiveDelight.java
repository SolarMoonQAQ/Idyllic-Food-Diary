package cn.solarmoon.immersive_delight;

import cn.solarmoon.immersive_delight.api.registry.core.BaseFMLEventRegistry;
import cn.solarmoon.immersive_delight.api.registry.Capabilities;
import cn.solarmoon.immersive_delight.common.registry.*;
import cn.solarmoon.immersive_delight.common.registry.client.*;
import cn.solarmoon.immersive_delight.compat.appleskin.AppleSkin;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.init.Config;
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

    public ImmersiveDelight() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //物品
        new IMItems().register(bus);
        //方块
        new IMBlocks().register(bus);
        //方块实体
        new IMEntityBlocks().register(bus);
        //流体（就这个没base成功，全是静态搞不定）
        new IMFluids.specialRegister().register(bus);
        //药水效果
        new IMEffects().register(bus);
        //TBA栏
        new IMCreativeModeTab().register(bus);
        //配方
        new IMRecipes().register(bus);
        //粒子
        new IMParticles().register(bus);
        //音效
        new IMSounds().register(bus);
        //生成
        new IMFeatures().register(bus);
        //生物
        new IMEntityTypes().register(bus);
        //网络包
        new IMPacks().register();

        //客户端事件
        new IMClientEvents().register(bus);
        //双端事件
        new IMCommonEvents().register(bus);
        new Capabilities().register(bus);
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
        Config.register();

        //—————————————————————————联动—————————————————————————//
        new FarmersDelight().register(bus);
        new AppleSkin().register(bus);
        new Create().register(bus);
    }

}