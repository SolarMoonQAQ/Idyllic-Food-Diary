package cn.solarmoon.immersive_delight.data.loot_tables;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.HashSet;
import java.util.Set;

public class IMBlockLoots extends BlockLootSubProvider {

    private final Set<Block> generatedLootTables = new HashSet<>();

    public IMBlockLoots() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(IMBlocks.FLATBREAD_DOUGH.get());
        dropSelf(IMBlocks.WHEAT_DOUGH.get());
        dropSelf(IMBlocks.CANGSHU_MUTTON_SOUP.get());

        dropSelf(IMBlocks.DURIAN.get());
        dropSelf(IMBlocks.GARLIC.get());
        //树苗
        dropSelf(IMBlocks.DURIAN_SAPLING.get());
        dropSelf(IMBlocks.APPLE_SAPLING.get());
        //实体方块
        dropSelf(IMBlocks.CELADON_CUP.get());
        dropSelf(IMBlocks.JADE_CHINA_CUP.get());
        dropSelf(IMBlocks.KETTLE.get());
        dropSelf(IMBlocks.SOUP_POT.get());
        dropSelf(IMBlocks.CUTTING_BOARD.get());



        //作物专项
        //类浆果丛类，用打的方式只掉种子
        dropOther(IMBlocks.BLACK_TEA_PLANT.get(), IMItems.BLACK_TEA_SEEDS.get());
        dropOther(IMBlocks.GREEN_TEA_PLANT.get(), IMItems.GREEN_TEA_SEEDS.get());
        dropOther(IMBlocks.APPLE_CROP.get(), IMItems.APPLE_CORE.get());
        dropOther(IMBlocks.DURIAN_CROP.get(), IMItems.DURIAN_CORE.get());
        //类小麦类，正常作物
        cropDrop(IMBlocks.GARLIC_CROP.get(), IMItems.GARLIC.get(), Items.AIR); //大蒜不掉种子（因为是蒜瓣）
    }

    public void cropDrop(Block block, Item product, Item seed) {
        add(block, createCropDrops(block, product, seed, cropConditionBuilder(block, CropBlock.AGE)));
    }

    public LootItemCondition.Builder cropConditionBuilder(Block block, IntegerProperty ageProperty) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(ageProperty, ((CropBlock)block).getMaxAge())
                );
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
