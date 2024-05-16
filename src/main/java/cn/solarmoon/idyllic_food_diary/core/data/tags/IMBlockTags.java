package cn.solarmoon.idyllic_food_diary.core.data.tags;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IMBlockTags extends BlockTagsProvider {

    public IMBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IdyllicFoodDiary.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        //擀面杖快速收割
        tag(ROLLABLE).add(
                IMBlocks.WHEAT_DOUGH.get(),
                IMBlocks.FLATBREAD_DOUGH.get(),
                Blocks.CAKE,
                IMBlocks.STEAMED_BUN.get(),
                IMBlocks.ROASTED_NAAN.get(),
                IMBlocks.SHORTENING_DOUGH.get(),
                IMBlocks.BAI_JI_BUN.get(),
                IMBlocks.PIE_CRUST.get(),
                IMBlocks.ROASTED_BAI_JI_BUN.get(),
                IMBlocks.CUSTARD_TART.get()
        ).replace(false);
        //可被镐子挖
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                IMBlocks.KETTLE.get(),
                IMBlocks.SOUP_POT.get(),
                IMBlocks.GRILL.get(),
                IMBlocks.PORCELAIN_PLATE.get(),
                IMBlocks.LONG_PORCELAIN_PLATE.get(),
                IMBlocks.SERVICE_PLATE.get(),
                IMBlocks.CELADON_CUP.get(),
                IMBlocks.JADE_CHINA_CUP.get()
        ).replace(false);
        //可被斧子挖
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                IMBlocks.DURIAN.get(),
                IMBlocks.CUTTING_BOARD.get(),
                IMBlocks.STEAMER_BASE.get(),
                IMBlocks.STEAMER.get(),
                IMBlocks.STEAMER_LID.get(),
                IMBlocks.BOWL.get(),
                IMBlocks.WOODEN_PLATE.get(),
                IMBlocks.LONG_WOODEN_PLATE.get()
        ).replace(false);
        //可被刀挖
        tag(MINEABLE_WITH_CLEAVER).add(
                Blocks.COBWEB
        )
                .addTag(BlockTags.WOOL)
                .addTag(BlockTags.WOOL_CARPETS)
                .addTag(BlockTags.MINEABLE_WITH_AXE)
                .replace(false);
        //热源
        tag(HEAT_SOURCE).add(
                Blocks.FIRE,
                Blocks.MAGMA_BLOCK,
                Blocks.LAVA,
                Blocks.CAMPFIRE,
                Blocks.SOUL_CAMPFIRE,
                Blocks.FURNACE
        ).replace(false);
        //汤容器
        tag(SOUP_CONTAINER).add(
                IMBlocks.SOUP_POT.get()
        ).replace(false);
        //砧板大类（决定了菜刀是否可以切割该tag的容器内的第一个物品）
        tag(CUTTING_BOARD).add(
                IMBlocks.CUTTING_BOARD.get()
        ).replace(false);
        //小型花
        tag(BlockTags.SMALL_FLOWERS).add(
                IMBlocks.WILD_GARLIC.get(),
                IMBlocks.WILD_GINGER.get(),
                IMBlocks.WILD_SPRING_ONION.get()
        );
    }

    public static final TagKey<Block> ROLLABLE = blockTag("rollable");
    public static final TagKey<Block> HEAT_SOURCE = blockTag("heat_source");
    public static final TagKey<Block> MINEABLE_WITH_CLEAVER = blockTag("mineable/cleaver");
    public static final TagKey<Block> SOUP_CONTAINER = blockTag("soup_container");
    public static final TagKey<Block> CUTTING_BOARD = blockTag("cutting_board");

    private static TagKey<Block> blockTag(String path) {
        return BlockTags.create(new ResourceLocation(IdyllicFoodDiary.MOD_ID, path));
    }

    private static TagKey<Block> forgeBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("forge", path));
    }

}

