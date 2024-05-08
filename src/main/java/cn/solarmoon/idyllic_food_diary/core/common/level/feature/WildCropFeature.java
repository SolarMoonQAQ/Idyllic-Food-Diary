package cn.solarmoon.idyllic_food_diary.core.common.level.feature;

import cn.solarmoon.idyllic_food_diary.core.common.level.config.WildCropConfig;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseBushCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Credit: Farmer's delight - <a href="https://github.com/vectorwing/FarmersDelight/tree/1.20">...</a>
 */
public class WildCropFeature extends Feature<WildCropConfig> {

    public WildCropFeature() {
        super(WildCropConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<WildCropConfig> context) {
        WildCropConfig config = context.config();
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
}
