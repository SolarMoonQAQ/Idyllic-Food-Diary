package cn.solarmoon.immersive_delight.client;

import cn.solarmoon.immersive_delight.client.ItemRenderer.PerspectiveBakedModelRenderer;
import cn.solarmoon.immersive_delight.client.ItemRenderer.TankableTooltipRenderer;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class IMItemRenderers {

    /**
     * tooltip渲染注册
     */
    @SubscribeEvent
    public static void tooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(TankableTooltipRenderer.TankTooltip.class, TankableTooltipRenderer::new);
    }

    /**
     * tooltip渲染实际应用
     */
    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        TankableTooltipRenderer.TankTooltip tankTooltip = new TankableTooltipRenderer.TankTooltip(event.getItemStack());
        //位置限定在系列名之上
        event.getTooltipElements().add(Math.max(event.getTooltipElements().size() - 1, 1) , Either.right(tankTooltip));
    }

    private static final List<Pair<ModelResourceLocation, ModelResourceLocation>> PERSPECTIVE_MODEL_LIST = Lists.newArrayList();

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addInHandModel(ROLLING_PIN.get());
        }
    }

    @SubscribeEvent
    public static void onBakedModel(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        for (Pair<ModelResourceLocation, ModelResourceLocation> pair : PERSPECTIVE_MODEL_LIST) {
            PerspectiveBakedModelRenderer model = new PerspectiveBakedModelRenderer(registry.get(pair.getLeft()), registry.get(pair.getRight()));
            registry.put(pair.getLeft(), model);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        PERSPECTIVE_MODEL_LIST.forEach((pair) -> event.register(pair.getRight()));
    }

    public static void addInHandModel(Item item) {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(item);
        if (res != null) {
            ModelResourceLocation rawName = new ModelResourceLocation(res, "inventory");
            ModelResourceLocation inHandName = new ModelResourceLocation(res.getNamespace(), res.getPath() + "_in_hand", "inventory");
            PERSPECTIVE_MODEL_LIST.add(Pair.of(rawName, inHandName));
        }
    }

}

