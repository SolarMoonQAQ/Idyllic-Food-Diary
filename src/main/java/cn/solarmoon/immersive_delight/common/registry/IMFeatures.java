package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.level.config.WildCropConfig;
import cn.solarmoon.immersive_delight.common.level.feature.AppleTreeFeature;
import cn.solarmoon.immersive_delight.common.level.feature.DurianTreeFeature;
import cn.solarmoon.immersive_delight.common.level.feature.WildCropFeature;
import cn.solarmoon.solarmoon_core.common.level.feature.configuration.BaseTreeConfig;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.FeatureEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;

public enum IMFeatures implements IRegister {
    INSTANCE;

    public static final FeatureEntry<BaseTreeConfig> APPLE_TREE = ImmersiveDelight.REGISTRY.feature()
            .id("apple_tree")
            .bound(AppleTreeFeature::new)
            .config(() -> BaseTreeConfig.createBaseTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 5, 3))
            .placement(() -> List.of(
                    PlacementUtils.countExtra(0, 0.1F, 1),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<BaseTreeConfig> DURIAN_TREE = ImmersiveDelight.REGISTRY.feature()
            .id("durian_tree")
            .bound(DurianTreeFeature::new)
            .config(() -> BaseTreeConfig.createBaseTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, 11, 3))
            .placement(() -> List.of(
                    PlacementUtils.countExtra(0, 0.1F, 1),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING),
                    BiomeFilter.biome()
            ))
            .build();

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);
    public static final BlockPos BLOCK_ABOVE = new BlockPos(0, 1, 0);

    public static final FeatureEntry<WildCropConfig> GREEN_TEA = ImmersiveDelight.REGISTRY.feature()
            .id("green_tea")
            .bound(WildCropFeature::new)
            .config(() -> WildCropConfig.createBaseWithAge(5, IMBlocks.GREEN_TEA_PLANT.get(), Blocks.FERN, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<WildCropConfig> BLACK_TEA = ImmersiveDelight.REGISTRY.feature()
            .id("black_tea")
            .bound(WildCropFeature::new)
            .config(() -> WildCropConfig.createBaseWithAge(5, IMBlocks.BLACK_TEA_PLANT.get(), Blocks.LILY_OF_THE_VALLEY, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<WildCropConfig> WILD_GARLIC = ImmersiveDelight.REGISTRY.feature()
            .id("wild_garlic")
            .bound(WildCropFeature::new)
            .config(() -> WildCropConfig.createBase(IMBlocks.WILD_GARLIC.get(), Blocks.GRASS, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

}
