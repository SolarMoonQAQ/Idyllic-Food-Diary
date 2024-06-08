package cn.solarmoon.idyllic_food_diary.data;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import cn.solarmoon.solarmoon_core.api.common.block.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseBushCropBlock;
import cn.solarmoon.solarmoon_core.api.common.block.crop.INoLimitAgeBlock;
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
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
        dropSelf(IMBlocks.SHORTENING_DOUGH.get());
        dropSelf(IMBlocks.BAI_JI_BUN.get());
        dropSelf(IMBlocks.PIE_CRUST.get());
        dropSelf(IMBlocks.ROASTED_BAI_JI_BUN.get());
        dropSelf(IMBlocks.CUSTARD_TART.get());
        dropSelf(IMBlocks.CANG_SHU_MUTTON_SOUP.get());
        dropSelf(IMBlocks.BEETROOT_SOUP.get());
        dropSelf(IMBlocks.MUSHROOM_STEW.get());
        dropSelf(IMBlocks.SEAWEED_EGG_DROP_SOUP.get());
        dropSelf(IMBlocks.PUMPKIN_SOUP.get());
        dropSelf(IMBlocks.EGG_LIQUID_BOWL.get());
        dropSelf(IMBlocks.STEAMED_EGG_CUSTARD.get());
        dropSelf(IMBlocks.STEAMED_BUN.get());
        dropSelf(IMBlocks.SERVICE_PLATE.get());
        dropSelf(IMBlocks.ROASTED_NAAN.get());
        dropSelf(IMBlocks.STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC.get());
        dropSelf(IMBlocks.STEAMED_CHICKEN_WITH_MUSHROOM.get());
        dropSelf(IMBlocks.STEAMED_CHICKEN_WITH_MUSHROOM_BOWL.get());
        dropSelf(IMBlocks.BEGGARS_CHICKEN.get());
        dropSelf(IMBlocks.UNCOOKED_CUSTARD_TART.get());
        dropSelf(IMBlocks.UNCOOKED_STEAMED_PUMPKIN_WITH_CHOPPED_GARLIC.get());
        dropSelf(IMBlocks.UNCOOKED_BEGGARS_CHICKEN.get());
        dropSelf(IMBlocks.STOVE.get());

        dropBedPart(IMBlocks.UNCOOKED_ROASTED_SUCKLING_PIG.get());
        dropBedPart(IMBlocks.STEAMED_SALMON.get());
        dropBedPart(IMBlocks.ROASTED_SUCKLING_PIG.get());


        dropSelf(IMBlocks.DURIAN.get());
        dropSelf(IMBlocks.BOWL.get());
        dropSelf(IMBlocks.PORCELAIN_PLATE.get());
        dropSelf(IMBlocks.WOODEN_PLATE.get());

        dropBedPart(IMBlocks.LONG_PORCELAIN_PLATE.get());
        dropBedPart(IMBlocks.LONG_WOODEN_PLATE.get());

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
        dropSelf(IMBlocks.COOKING_POT.get());
        dropSelf(IMBlocks.CUTTING_BOARD.get());
        dropSelf(IMBlocks.GRILL.get());
        dropSelf(IMBlocks.STEAMER.get());
        dropSelf(IMBlocks.STEAMER_LID.get());
        dropSelf(IMBlocks.SERVICE_PLATE.get());

        //作物专项
        //类浆果丛类
        bushDrop(IMBlocks.BLACK_TEA_PLANT.get(), IMItems.BLACK_TEA_LEAF.get(), IMItems.BLACK_TEA_SEEDS.get());
        bushDrop(IMBlocks.GREEN_TEA_PLANT.get(), IMItems.GREEN_TEA_LEAF.get(), IMItems.GREEN_TEA_SEEDS.get());
        bushDrop(IMBlocks.APPLE_CROP.get(), Items.APPLE, IMItems.APPLE_CORE.get());
        bushDrop(IMBlocks.DURIAN_CROP.get(), IMItems.DURIAN.get(), IMItems.DURIAN_CORE.get());
        //类小麦类，正常作物
        garlicDrop(IMBlocks.GARLIC_CROP.get(), IMItems.GARLIC.get(), IMItems.GARLIC_SPROUTS.get(), IMItems.GARLIC_CLOVE.get()); //大蒜使用特殊掉落
        cropDrop(IMBlocks.GINGER_CROP.get(), Items.AIR, IMItems.GINGER.get()); //生姜种子就是自己
        cropDrop(IMBlocks.SPRING_ONION_CROP.get(), Items.AIR, IMItems.SPRING_ONION.get()); //小葱种子就是自己
    }

    private void cropDrop(Block block, Item product, Item seed) {
        add(block, createCropDrops(block, product, seed, cropConditionBuilder(block)));
    }

    private void garlicDrop(Block block, Item product, Item product2, Item seed) {
        add(block, createGarlicDrops(block, product, product2, seed, cropConditionBuilder(block)));
    }

    private void bushDrop(Block block, Item product, Item seed) {
        add(block, createBushDrops(block, product, seed, bushConditionBuilder(block)));
    }

    /**
     * 大蒜专用掉落，未成熟只掉落蒜瓣，成熟掉落大蒜和蒜苗，不吃幸运
     */
    protected LootTable.Builder createGarlicDrops(Block block, Item product, Item product2, Item seed, LootItemCondition.Builder condition) {
        return this.applyExplosionDecay(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product)
                                                .when(condition)
                                                .otherwise(LootItem.lootTableItem(seed)))
                        )
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product2)
                                                .when(condition)
                                )
                        )
        );
    }

    /**
     * 类浆果丛掉落，未成熟只掉作物种子，成熟后变为掉一个果实（默认都掉1个，此类一般右键摘取，右键情况下才有可能摘取更多）
     */
    protected LootTable.Builder createBushDrops(Block block, Item product, Item seed, LootItemCondition.Builder condition) {
        return this.applyExplosionDecay(block,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().add(
                                        LootItem.lootTableItem(product)
                                                .when(condition)
                                                .otherwise(LootItem.lootTableItem(seed)))
                        )
        );
    }

    private LootItemCondition.Builder bushConditionBuilder(Block block) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(INoLimitAgeBlock.AGE, ((BaseBushCropBlock)block).getMaxAge())
                );
    }

    private LootItemCondition.Builder cropConditionBuilder(Block block) {
        return LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(CropBlock.AGE, ((CropBlock)block).getMaxAge())
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
