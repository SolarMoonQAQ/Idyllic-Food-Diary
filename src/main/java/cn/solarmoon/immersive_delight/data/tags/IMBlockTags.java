package cn.solarmoon.immersive_delight.data.tags;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.registry.IMBlocks;
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
        super(output, lookupProvider, ImmersiveDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        //擀面杖快速收割
        tag(CAN_BE_ROLLED).add(
                IMBlocks.WHEAT_DOUGH.get(),
                IMBlocks.FLATBREAD_DOUGH.get(),
                Blocks.CAKE
        ).replace(false);
        //可被镐子挖
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                IMBlocks.KETTLE.get(),
                IMBlocks.SOUP_POT.get()
        ).replace(false);
        //可被斧子挖
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                IMBlocks.DURIAN.get(),
                IMBlocks.CUTTING_BOARD.get()
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
        //砧板大类
        tag(CUTTING_BOARD).add(
                IMBlocks.CUTTING_BOARD.get()
        ).replace(false);
    }

    public static final TagKey<Block> CAN_BE_ROLLED = blockTag("can_be_rolled");
    public static final TagKey<Block> HEAT_SOURCE = blockTag("heat_source");
    public static final TagKey<Block> MINEABLE_WITH_CLEAVER = blockTag("mineable/cleaver");
    public static final TagKey<Block> SOUP_CONTAINER = blockTag("soup_container");
    public static final TagKey<Block> CUTTING_BOARD = blockTag("cutting_board");

    private static TagKey<Block> blockTag(String path) {
        return BlockTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, path));
    }

    private static TagKey<Block> forgeBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("forge", path));
    }

}

