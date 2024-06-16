package cn.solarmoon.idyllic_food_diary.element.level;

import cn.solarmoon.solarmoon_core.api.block_base.BaseBushCropBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Credit: Farmer's delight - <a href="https://github.com/vectorwing/FarmersDelight/tree/1.20">...</a>
 */
public class WildCropFeature extends Feature<WildCropFeature.Config> {

    public WildCropFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        Config config = context.config();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        int i = 0;
        int tries = config.tries();
        int xzSpread = config.xzSpread() + 1;
        int ySpread = config.ySpread() + 1;

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        List<BlockPos> placedBlocks = new ArrayList<>();

        Holder<PlacedFeature> floorFeature = config.floorFeature();
        if (floorFeature != null) {
            for (int j = 0; j < tries; ++j) {
                mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
                if (config.floorFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
                    placedBlocks.add(mutablePos.immutable());
                    ++i;
                }
            }
        }

        for (int k = 0; k < tries; ++k) {
            int shorterXZ = xzSpread - 2;
            mutablePos.setWithOffset(origin, random.nextInt(shorterXZ) - random.nextInt(shorterXZ), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(shorterXZ) - random.nextInt(shorterXZ));
            if (config.primaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
                placedBlocks.add(mutablePos.immutable());
                ++i;
            }
        }

        for (int l = 0; l < tries; ++l) {
            mutablePos.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
            if (config.secondaryFeature().value().place(level, context.chunkGenerator(), random, mutablePos)) {
                placedBlocks.add(mutablePos.immutable());
                ++i;
            }
        }

        if (config.ageRandom() > 0) {
            for (BlockPos pos : placedBlocks) {
                BlockState state = level.getBlockState(pos);
                if (state.getValues().get(BaseBushCropBlock.AGE) != null) {
                    int age = random.nextInt(config.ageRandom());
                    state = state.setValue(BaseBushCropBlock.AGE, age); // 创建一个新的BlockState
                    if (state.getValues().get(IHorizontalFacingBlock.FACING) != null) {
                        int index = random.nextInt(4);
                        Direction facing = Direction.NORTH;
                        switch (index) {
                            case 1 -> facing = Direction.EAST;
                            case 2 -> facing = Direction.SOUTH;
                            case 3 -> facing = Direction.WEST;
                        }
                        state = state.setValue(IHorizontalFacingBlock.FACING, facing); //同时使方向也具有随机性
                    }
                    level.setBlock(pos, state, 3);
                }
            }
        }

        return i > 0;
    }


    /**
     * Credit: Farmer's delight - <a href="https://github.com/vectorwing/FarmersDelight/tree/1.20">...</a>
     */
    public static record Config(int tries, int xzSpread, int ySpread, int ageRandom, Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature
    ) implements FeatureConfiguration {

        public static final Codec<Config> CODEC = RecordCodecBuilder.create((config) -> config.group(
                ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter(Config::tries),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter(Config::xzSpread),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(Config::ySpread),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("age_random").orElse(0).forGetter(Config::ageRandom),
                PlacedFeature.CODEC.fieldOf("primary_feature").forGetter(Config::primaryFeature),
                PlacedFeature.CODEC.fieldOf("secondary_feature").forGetter(Config::secondaryFeature),
                PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter(floorConfig -> Optional.ofNullable(floorConfig.floorFeature))
        ).apply(config, (tries, xzSpread, yspread, agerandom, primary, secondary, floor) -> floor.map(placedFeatureHolder -> new Config(tries, xzSpread, yspread, agerandom, primary, secondary, placedFeatureHolder)).orElseGet(() -> new Config(tries, xzSpread, yspread, agerandom, primary, secondary, null))));

        public Config(int tries, int xzSpread, int ySpread, int ageRandom, Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature) {
            this.tries = tries;
            this.xzSpread = xzSpread;
            this.ySpread = ySpread;
            this.ageRandom = ageRandom;
            this.primaryFeature = primaryFeature;
            this.secondaryFeature = secondaryFeature;
            this.floorFeature = floorFeature;
        }

        public int tries() {
            return this.tries;
        }

        public int xzSpread() {
            return this.xzSpread;
        }

        public int ySpread() {
            return this.ySpread;
        }

        public Holder<PlacedFeature> primaryFeature() {
            return this.primaryFeature;
        }

        public Holder<PlacedFeature> secondaryFeature() {
            return this.secondaryFeature;
        }

        public Holder<PlacedFeature> floorFeature() {
            return this.floorFeature;
        }

        public static Config createBase(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
            return new Config(64, 6, 3, 0, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
        }

        public static Config createBaseWithAge(int ageRandom, Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
            return new Config(64, 6, 3, ageRandom, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
        }

        public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
            return PlacementUtils.filtered(
                    SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                    BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
        }

    }
}
