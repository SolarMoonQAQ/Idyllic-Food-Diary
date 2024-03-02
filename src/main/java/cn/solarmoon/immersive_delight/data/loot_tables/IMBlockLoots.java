package cn.solarmoon.immersive_delight.data.loot_tables;

import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
        dropSelf(IMBlocks.BEETROOT_SOUP.get());
        dropSelf(IMBlocks.MUSHROOM_STEW.get());
        dropSelf(IMBlocks.SEAWEED_EGG_DROP_SOUP.get());
        dropSelf(IMBlocks.PUMPKIN_SOUP.get());
        dropSelf(IMBlocks.EGG_LIQUID_BOWL.get());
        dropSelf(IMBlocks.STEAMED_EGG_CUSTARD.get());
        dropSelf(IMBlocks.STEAMED_BUN.get());
        dropSelf(IMBlocks.SERVICE_PLATE.get());

        dropBedPart(IMBlocks.UNCOOKED_STEAMED_SALMON.get());
        dropBedPart(IMBlocks.STEAMED_SALMON.get());


        dropSelf(IMBlocks.DURIAN.get());
        dropSelf(IMBlocks.BOWL.get());

        dropBedPart(IMBlocks.LONG_PORCELAIN_PLATE.get());

        //野生作物
        dropWildCrop(IMBlocks.WILD_GARLIC.get(), IMItems.GARLIC_CLOVE.get(), IMItems.GARLIC.get());
        dropWildCrop(IMBlocks.WILD_GINGER.get(), IMItems.GINGER.get(), IMItems.GINGER.get());
        dropWildCrop(IMBlocks.WILD_SPRING_ONION.get(), IMItems.SPRING_ONION.get(), IMItems.SPRING_ONION.get());

        //树苗
        dropSelf(IMBlocks.DURIAN_SAPLING.get());
        dropSelf(IMBlocks.APPLE_SAPLING.get());
        //实体方块
        dropSelf(IMBlocks.CELADON_CUP.get());
        dropSelf(IMBlocks.JADE_CHINA_CUP.get());
        dropSelf(IMBlocks.KETTLE.get());
        dropSelf(IMBlocks.SOUP_POT.get());
        dropSelf(IMBlocks.CUTTING_BOARD.get());
        dropSelf(IMBlocks.GRILL.get());
        //dropSelf(IMBlocks.STEAMER.get()); <-自定义
        dropSelf(IMBlocks.STEAMER_BASE.get());
        dropSelf(IMBlocks.STEAMER_LID.get());
        dropSelf(IMBlocks.SERVICE_PLATE.get());

        //作物专项
        //类浆果丛类，用打的方式只掉种子
        dropOther(IMBlocks.BLACK_TEA_PLANT.get(), IMItems.BLACK_TEA_SEEDS.get());
        dropOther(IMBlocks.GREEN_TEA_PLANT.get(), IMItems.GREEN_TEA_SEEDS.get());
        dropOther(IMBlocks.APPLE_CROP.get(), IMItems.APPLE_CORE.get());
        dropOther(IMBlocks.DURIAN_CROP.get(), IMItems.DURIAN_CORE.get());
        //类小麦类，正常作物
        cropDrop(IMBlocks.GARLIC_CROP.get(), IMItems.GARLIC.get(), Items.AIR); //大蒜不掉种子（因为是蒜瓣）
        cropDrop(IMBlocks.GINGER_CROP.get(), Items.AIR, IMItems.GINGER.get()); //生姜种子就是自己
        cropDrop(IMBlocks.SPRING_ONION_CROP.get(), Items.AIR, IMItems.SPRING_ONION.get()); //小葱种子就是自己
    }

    private void cropDrop(Block block, Item product, Item seed) {
        add(block, createCropDrops(block, product, seed, cropConditionBuilder(block, CropBlock.AGE)));
    }

    private LootItemCondition.Builder cropConditionBuilder(Block block, IntegerProperty ageProperty) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(ageProperty, ((CropBlock)block).getMaxAge())
                );
    }

    private void dropBedPart(Block block) {
        this.add(block, (builder) -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("pool1")
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(block))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(IBedPartBlock.PART, BedPart.HEAD)))));
    }

    private void dropWildCrop(Block wildCrop, ItemLike seed, ItemLike product) {
        this.add(wildCrop, (builder) -> LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .name("pool1")
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(product)
                                .when(LootItemRandomChanceCondition.randomChance(0.2F))
                                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)))))
                )
                .withPool(LootPool.lootPool()
                        .name("pool2")
                        .setRolls(ConstantValue.exactly(1))
                        .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(wildCrop)
                                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS))),
                                        LootItem.lootTableItem(seed)
                                                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))
                                                .apply(ApplyExplosionDecay.explosionDecay())
                                )
                        )
                )
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
