package cn.solarmoon.immersive_delight;

import cn.solarmoon.immersive_delight.common.registry.*;
import cn.solarmoon.immersive_delight.common.registry.client.*;
import cn.solarmoon.immersive_delight.compat.apple_skin.AppleSkin;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
import cn.solarmoon.immersive_delight.data.fluid_effects.FluidEffectsBuilder;
import cn.solarmoon.immersive_delight.data.fluid_foods.FluidFoodsBuilder;
import cn.solarmoon.immersive_delight.init.Config;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
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

        bus.addListener(this::onFMLCommonSetupEvent);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::onFMLClientSetupEvent);
            bus.addListener(this::registerEntityRenderers);
            bus.addListener(this::tooltipRegister);
            bus.addListener(this::registerParticles);
            bus.addListener(this::registerGUI);
        }

        MinecraftForge.EVENT_BUS.register(this);

        //配置文件
        Config.register();

        //—————————————————————————联动—————————————————————————//
        new FarmersDelight().register(bus);
        new AppleSkin().register(bus);
        new Create().register(bus);

    }

    @SubscribeEvent
    public void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        //双端事件
        new IMCommonEvents().register();
        //网络包
        new IMPacks().register();
    }

    @SubscribeEvent
    public void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        //客户端事件
        new IMClientEvents().register();
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        //方块实体渲染
        new IMBlockEntityRenderers().register(event);
        //实体渲染
        new IMEntityRenderers().register(event);
    }

    @SubscribeEvent
    public void registerParticles(RegisterParticleProvidersEvent event) {
        //粒子
        new IMParticles().register(event);
    }

    @SubscribeEvent
    public void registerGUI(RegisterGuiOverlaysEvent event) {
        //gui
        new IMGui().register(event);
    }

    /**
     * tooltip渲染注册
     */
    @SubscribeEvent
    public void tooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        //tooltip
        new IMTooltipRenderers().register(event);
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        //液体效果
        event.addListener(new FluidEffectsBuilder());
        //流体类食物（碗装汤类）
        event.addListener(new FluidFoodsBuilder());
    }

}