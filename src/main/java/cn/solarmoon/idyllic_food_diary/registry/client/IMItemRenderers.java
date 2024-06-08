package cn.solarmoon.idyllic_food_diary.registry.client;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.food.FoodEntityBlock;
import cn.solarmoon.idyllic_food_diary.feature.visual.container_mapping_combination.ContainableFoodBakedModel;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.client.model.item.PerspectiveBakedModelRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class IMItemRenderers {

    private static final List<Pair<ModelResourceLocation, ModelResourceLocation>> PERSPECTIVE_MODEL_LIST = Lists.newArrayList();
    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            addInHandModel(IMItems.ROLLING_PIN.get());
            addInHandModel(IMItems.CHINESE_CLEAVER.get());
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
    public static void putContainerCombinedModel(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        var list = IdyllicFoodDiary.REGISTRY.itemRegister.getEntries().stream()
                .map(RegistryObject::get)
                .filter(item -> Block.byItem(item) instanceof FoodEntityBlock)
                .toList();
        for (var e : list) {
            ModelResourceLocation res = new ModelResourceLocation(ForgeRegistries.ITEMS.getKey(e), "inventory");
            registry.put(res, new ContainableFoodBakedModel(registry.get(res)));
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

