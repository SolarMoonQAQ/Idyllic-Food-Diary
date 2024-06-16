package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.level.AppleTreeFeature;
import cn.solarmoon.idyllic_food_diary.element.level.DurianTreeFeature;
import cn.solarmoon.idyllic_food_diary.element.level.WildCropFeature;
import cn.solarmoon.solarmoon_core.api.entry.common.FeatureEntry;
import cn.solarmoon.solarmoon_core.api.level_feature.BaseTreeFeature;
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

public class IMFeatures {
    public static void register() {}

    public static final FeatureEntry<BaseTreeFeature.Config> APPLE_TREE = IdyllicFoodDiary.REGISTRY.feature()
            .id("apple_tree")
            .bound(AppleTreeFeature::new)
            .config(() -> BaseTreeFeature.Config.createBaseTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 5, 3))
            .placement(() -> List.of(
                    PlacementUtils.countExtra(0, 0.1F, 1),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING),
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<BaseTreeFeature.Config> DURIAN_TREE = IdyllicFoodDiary.REGISTRY.feature()
            .id("durian_tree")
            .bound(DurianTreeFeature::new)
            .config(() -> BaseTreeFeature.Config.createBaseTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, 11, 3))
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

    public static final FeatureEntry<WildCropFeature.Config> GREEN_TEA = IdyllicFoodDiary.REGISTRY.feature()
            .id("green_tea")
            .bound(WildCropFeature::new)
            .config(() -> WildCropFeature.Config.createBaseWithAge(5, IMBlocks.GREEN_TEA_PLANT.get(), Blocks.FERN, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<WildCropFeature.Config> WILD_SPRING_ONION = IdyllicFoodDiary.REGISTRY.feature()
            .id("wild_spring_onion")
            .bound(WildCropFeature::new)
            .config(() -> WildCropFeature.Config.createBase(IMBlocks.WILD_SPRING_ONION.get(), Blocks.ALLIUM, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<WildCropFeature.Config> WILD_GINGER = IdyllicFoodDiary.REGISTRY.feature()
            .id("wild_ginger")
            .bound(WildCropFeature::new)
            .config(() -> WildCropFeature.Config.createBase(IMBlocks.WILD_GINGER.get(), Blocks.GRASS, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

    public static final FeatureEntry<WildCropFeature.Config> WILD_GARLIC = IdyllicFoodDiary.REGISTRY.feature()
            .id("wild_garlic")
            .bound(WildCropFeature::new)
            .config(() -> WildCropFeature.Config.createBase(IMBlocks.WILD_GARLIC.get(), Blocks.GRASS, BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)))
            .placement(() -> List.of(
                    RarityFilter.onAverageOnceEvery(120),
                    InSquarePlacement.spread(),
                    PlacementUtils.HEIGHTMAP,
                    BiomeFilter.biome()
            ))
            .build();

}
