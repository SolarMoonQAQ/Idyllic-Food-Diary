package cn.solarmoon.immersive_delight.data.tags;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.registry.IMItems;
import cn.solarmoon.immersive_delight.compat.farmersdelight.registry.FDItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IMItemTags extends ItemTagsProvider {

    public IMItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, ImmersiveDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        registerModTags();
    }

    protected void registerModTags() {
        //擀面杖配方
        tag(ROLLING_DOUGH).add(
                IMItems.WHEAT_DOUGH.get(),
                IMItems.FLATBREAD_DOUGH.get()
        ).addOptional(
                FDItems.WHEAT_DOUGH.getId()
        ).replace(false);
        //刀类物品
        tag(FORGE_KNIVES).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        tag(FD_KNIVES).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        //砍刀类物品
        tag(FORGE_CLEAVERS).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        //斧类物品
        tag(ItemTags.AXES).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        tag(FORGE_AXES).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        //剑类物品
        tag(ItemTags.SWORDS).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        tag(FORGE_SWORDS).add(
                IMItems.CHINESE_CLEAVER.get()
        ).replace(false);
        //茶类物品
        tag(TEA).add(
                IMItems.BLACK_TEA_LEAVES.get(),
                IMItems.GREEN_TEA_LEAVES.get()
        ).replace(false);
        tag(CUP).add(
                IMItems.CELADON_CUP.get(),
                IMItems.JADE_CHINA_CUP.get()
        ).replace(false);
        //TOOLTIP渲染流体框
        tag(TOOLTIP_FLUID_RENDER).add(
                IMItems.CELADON_CUP.get(),
                IMItems.JADE_CHINA_CUP.get(),
                IMItems.KETTLE.get(),
                IMItems.SOUP_POT.get()
        ).replace(false);
    }

    //特殊效果
    public static final TagKey<Item> ROLLING_DOUGH = itemTag("dough");
    public static final TagKey<Item> TOOLTIP_FLUID_RENDER = itemTag("tooltip_fluid_render");

    //一般配方标识等
    public static final TagKey<Item> TEA = itemTag("tea");
    public static final TagKey<Item> CUP = itemTag("cup");

    //forge通用
    public static final TagKey<Item> FORGE_AXES = itemForgeTag("tools/axes");
    public static final TagKey<Item> FORGE_SWORDS = itemForgeTag("tools/swords");
    public static final TagKey<Item> FORGE_KNIVES = itemForgeTag("tools/knives");
    public static final TagKey<Item> FORGE_CLEAVERS = itemForgeTag("tools/cleavers");

    public static final TagKey<Item> FD_KNIVES = itemFDTag("tools/knives");

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, path));
    }

    private static TagKey<Item> itemForgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

    private static TagKey<Item> itemFDTag(String path) {
        return ItemTags.create(new ResourceLocation("farmersdelight", path));
    }

}
