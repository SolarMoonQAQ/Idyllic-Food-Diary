package cn.solarmoon.idyllic_food_diary.data;

import cn.solarmoon.idyllic_food_diary.data.loot_tables.IMBlockLoots;
import cn.solarmoon.idyllic_food_diary.data.tags.IMBlockTags;
import cn.solarmoon.idyllic_food_diary.data.tags.IMFluidTags;
import cn.solarmoon.idyllic_food_diary.data.tags.IMItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary.MOD_ID;

/**
 * 用于加载数据
 */
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    /**
     * 在运行runData时加载数据
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        net.minecraft.data.DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        IMBlockTags blockTags = new IMBlockTags(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new IMItemTags(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new IMFluidTags(output, lookupProvider, helper));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(IMBlockLoots::new, LootContextParamSets.BLOCK)
        )));
    }

}
