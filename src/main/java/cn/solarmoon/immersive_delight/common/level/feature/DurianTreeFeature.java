package cn.solarmoon.immersive_delight.common.level.feature;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.registry.IMFeatures;
import cn.solarmoon.solarmoon_core.common.level.feature.abstract_feature.BaseTreeFeature;
import cn.solarmoon.solarmoon_core.common.level.feature.configuration.BaseTreeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.Nullable;

/**
 * 榴莲树，最低height为11
 */
public class DurianTreeFeature extends BaseTreeFeature {

    public DurianTreeFeature() {}

    @Override
    public void addSets(FeaturePlaceContext<BaseTreeConfig> context) {
        BaseTreeConfig config = context.config();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        int height = config.trunkPlacer.getTreeHeight(random);
        for (int i = 0; i < height; i++) {
            for (var logPos : BlockPos.betweenClosed(pos.offset(-6, i, -6), pos.offset(6, i, 6))) {
                double distance = Math.sqrt(Math.pow(pos.getX() - logPos.getX(), 2) + Math.pow(pos.getZ() - logPos.getZ(), 2));
                if (i == 0 && distance < 2.24) {
                    addLog(logPos);
                    addBottom(logPos.below());
                }
                if (i == 1 && distance < 2.01) addLog(logPos);
                if (i > 1 && distance < 1.01) addLog(logPos);
                if ((i == 5 || i > 7 ) && distance == 1) removeLog(logPos);
                for (int n = 2; n < height - 7; n++) {
                    if (i == n + 6) {
                        if (distance == n) {
                            addLog(logPos);
                            addProduct(logPos.below(), random, 0.7f);
                            if (n == height - 8) setBaseFoliage(logPos.above(2), 1, random);
                        }
                        if (distance == Math.sqrt(2 * Math.pow(n - 1, 2))) {
                            addLog(logPos);
                            addProduct(logPos.below(), random, 0.5f);
                            if (n == height - 8) setBaseFoliage(logPos.above(2), 1, random);
                            if (n >= 4 && height - 8 >= 6) setBaseFoliage(logPos.above(3), 1, random);
                        }
                    }
                }
                if (distance == 0 && i == height - 1) {
                    setBaseFoliage(logPos.above(2), 1, random);
                    setBaseFoliage(logPos.above(4), 1, random);
                }
            }
        }

    }

    @Override
    public BlockState getSapling() {
        return IMBlocks.DURIAN_SAPLING.get().defaultBlockState();
    }

    @Override
    public BlockState getProduct() {
        return IMBlocks.DURIAN_CROP.get().defaultBlockState();
    }

    public static class DurianTreeGrower extends AbstractTreeGrower {
        @Override
        @Nullable
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomIn, boolean beehiveIn) {
            return IMFeatures.DURIAN_TREE.getConfigKey();
        }
    }
}
