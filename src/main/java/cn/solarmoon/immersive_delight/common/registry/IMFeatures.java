package cn.solarmoon.immersive_delight.common.registry;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.level.feature.AppleTreeFeature;
import cn.solarmoon.immersive_delight.common.level.feature.DurianTreeFeature;
import cn.solarmoon.solarmoon_core.common.level.feature.configuration.BaseTreeConfig;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.FeatureEntry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;

public enum IMFeatures implements IRegister {
    INSTANCE;

    public static final FeatureEntry<BaseTreeConfig> APPLE_TREE = ImmersiveDelight.REGISTRY.feature()
            .id("apple_tree")
            .bound(AppleTreeFeature::new)
            .config(BaseTreeConfig.createBaseTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 5, 3))
            .placement(List.of(
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
            .config(BaseTreeConfig.createBaseTree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, 11, 3))
            .placement(List.of(
                    PlacementUtils.countExtra(0, 0.2F, 1),
                    SurfaceWaterDepthFilter.forMaxDepth(0),
                    PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                    PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING),
                    BiomeFilter.biome()
            ))
            .build();

}
