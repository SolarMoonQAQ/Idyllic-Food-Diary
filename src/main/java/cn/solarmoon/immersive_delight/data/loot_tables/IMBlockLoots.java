package cn.solarmoon.immersive_delight.data.loot_tables;

import cn.solarmoon.immersive_delight.common.IMBlocks;
import cn.solarmoon.immersive_delight.common.IMEntityBlocks;
import cn.solarmoon.immersive_delight.common.IMItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class IMBlockLoots extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet<>();

    public IMBlockLoots() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(IMEntityBlocks.CELADON_CUP.get());
        dropSelf(IMEntityBlocks.KETTLE.get());
        dropSelf(IMBlocks.FLATBREAD_DOUGH.get());
        dropSelf(IMBlocks.WHEAT_DOUGH.get());
        dropSelf(IMBlocks.DURIAN_BLOCK.get());
        dropSelf(IMBlocks.test.get());

        //这部分都是作物打掉掉种子
        dropOther(IMBlocks.BLACK_TEA_TREE.get(), IMItems.BLACK_TEA_SEEDS.get());
        dropOther(IMBlocks.GREEN_TEA_TREE.get(), IMItems.GREEN_TEA_SEEDS.get());
        dropOther(IMBlocks.APPLE.get(), IMItems.APPLE_CORE.get());
        dropOther(IMBlocks.DURIAN.get(), IMItems.DURIAN_CORE.get());
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        this.generatedLootTables.add(block);
        this.map.put(block.getLootTable(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return generatedLootTables;
    }

}
