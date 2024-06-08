package cn.solarmoon.idyllic_food_diary.element.level.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Credit: Farmer's delight - <a href="https://github.com/vectorwing/FarmersDelight/tree/1.20">...</a>
 */
public record WildCropConfig(int tries, int xzSpread, int ySpread, int ageRandom, Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature
) implements FeatureConfiguration {

    public static final Codec<WildCropConfig> CODEC = RecordCodecBuilder.create((config) -> config.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter(WildCropConfig::tries),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter(WildCropConfig::xzSpread),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(WildCropConfig::ySpread),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("age_random").orElse(0).forGetter(WildCropConfig::ageRandom),
            PlacedFeature.CODEC.fieldOf("primary_feature").forGetter(WildCropConfig::primaryFeature),
            PlacedFeature.CODEC.fieldOf("secondary_feature").forGetter(WildCropConfig::secondaryFeature),
            PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter(floorConfig -> Optional.ofNullable(floorConfig.floorFeature))
    ).apply(config, (tries, xzSpread, yspread, agerandom, primary, secondary, floor) -> floor.map(placedFeatureHolder -> new WildCropConfig(tries, xzSpread, yspread, agerandom, primary, secondary, placedFeatureHolder)).orElseGet(() -> new WildCropConfig(tries, xzSpread, yspread, agerandom, primary, secondary, null))));

    public WildCropConfig(int tries, int xzSpread, int ySpread, int ageRandom, Holder<PlacedFeature> primaryFeature, Holder<PlacedFeature> secondaryFeature, @Nullable Holder<PlacedFeature> floorFeature) {
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

    public static WildCropConfig createBase(Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
        return new WildCropConfig(64, 6, 3, 0, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
    }

    public static WildCropConfig createBaseWithAge(int ageRandom, Block primaryBlock, Block secondaryBlock, BlockPredicate plantedOn) {
        return new WildCropConfig(64, 6, 3, ageRandom, plantBlockConfig(primaryBlock, plantedOn), plantBlockConfig(secondaryBlock, plantedOn), null);
    }

    public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
        return PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
    }

}
