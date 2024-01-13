package cn.solarmoon.immersive_delight.data.worldgen;

import cn.solarmoon.immersive_delight.common.IMFeatures;
import cn.solarmoon.immersive_delight.common.level.feature.configuration.BaseTreeConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;

public class TreeBootstrap {

    @SuppressWarnings("unchecked")
    public static void Configured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        //苹果树
        register(context, IMFeatures.Configured.APPLE_TREE, IMFeatures.APPLE_TREE.get(),
                createTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES,
                        5, 3));
        //榴莲树
        register(context, IMFeatures.Configured.DURIAN_TREE, IMFeatures.DURIAN_TREE.get(),
                createTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES,
                        11, 3));
    }

    public static void Placed(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureGetter = context.lookup(Registries.CONFIGURED_FEATURE);

        //苹果树
        final Holder<ConfiguredFeature<?, ?>> APPLE_TREE = featureGetter.getOrThrow(IMFeatures.Configured.APPLE_TREE);
        register(context, IMFeatures.Placed.APPLE_TREE, APPLE_TREE,
                List.of(PlacementUtils.countExtra(0, 0.1F, 1),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                        BiomeFilter.biome()
                ));
        //榴莲树
        final Holder<ConfiguredFeature<?, ?>> DURIAN_TREE = featureGetter.getOrThrow(IMFeatures.Configured.DURIAN_TREE);
        register(context, IMFeatures.Placed.DURIAN_TREE, DURIAN_TREE,
                List.of(PlacementUtils.countExtra(0, 0.2F, 1),
                        SurfaceWaterDepthFilter.forMaxDepth(0),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING),
                        BiomeFilter.biome()
                ));
    }

    protected static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placement) {
        context.register(key, new PlacedFeature(feature, placement));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

    private static BaseTreeConfig createTree(Block log, Block leaves, int baseHeight, int heightRandomA) {
        return new BaseTreeConfig(BlockStateProvider.simple(log), BlockStateProvider.simple(leaves), new FancyTrunkPlacer(baseHeight, heightRandomA, 0));
    }

}
