package cn.solarmoon.idyllic_food_diary.core.data.tags;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IMItemTags extends ItemTagsProvider {

    public IMItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, IdyllicFoodDiary.MOD_ID, existingFileHelper);
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
        ).replace(false);
        tag(ROLLING_SHORTENING_DOUGH).add(
                IMItems.SHORTENING_DOUGH.get(),
                IMItems.PIE_CRUST.get(),
                IMItems.BAI_JI_BUN.get()
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
        tag(FORGE_TEA).add(
                IMItems.BLACK_TEA_LEAF.get(),
                IMItems.GREEN_TEA_LEAF.get()
        ).replace(false);
        //杯类（已知用于jei）
        tag(CUP).add(
                IMItems.CELADON_CUP.get(),
                IMItems.JADE_CHINA_CUP.get()
        ).replace(false);
        //TOOLTIP渲染流体框
        tag(TOOLTIP_FLUID_RENDER).add(
                IMItems.CELADON_CUP.get(),
                IMItems.JADE_CHINA_CUP.get(),
                IMItems.KETTLE.get()
        ).replace(false);
        //小型花
        copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        //面团
        tag(FORGE_DOUGH).add(
                IMItems.WHEAT_DOUGH.get()
        ).replace(false);
        //蛋
        tag(FORGE_EGGS).add(
                Items.EGG,
                IMItems.EGG_LIQUID_BOWL.get()
        ).replace(false);
        // 容器，使得同种菜可以被不同容器盛装
        // 大容器，双格
        tag(LARGE_CONTAINER).add(
                IMItems.LONG_PORCELAIN_PLATE.get(),
                IMItems.LONG_WOODEN_PLATE.get()
        );
        // 普通容器，类似盘子
        tag(MEDIUM_CONTAINER).add(
                IMItems.PORCELAIN_PLATE.get(),
                IMItems.WOODEN_PLATE.get()
        );
        // 小容器，类似碗
        tag(SMALL_CONTAINER).add(
                Items.BOWL
        );
        //汤容器，用于识别其中的液体能否被容器盛出
        copy(IMBlockTags.SOUP_CONTAINER, SOUP_CONTAINER);
        //奶桶类配方标识
        tag(FORGE_MILK).add(
                Items.MILK_BUCKET
        );
        // 调味料，决定是否可以放入调味罐中
        tag(SPICE).add(
                IMItems.SALT.get()
        );
    }

    //特殊效果
    public static final TagKey<Item> ROLLING_DOUGH = itemTag("dough");
    public static final TagKey<Item> ROLLING_SHORTENING_DOUGH = itemTag("shortening_dough");
    public static final TagKey<Item> TOOLTIP_FLUID_RENDER = itemTag("tooltip_fluid_render");
    public static final TagKey<Item> LARGE_CONTAINER = itemTag("large_container");
    public static final TagKey<Item> MEDIUM_CONTAINER = itemTag("medium_container");
    public static final TagKey<Item> SMALL_CONTAINER = itemTag("small_container");
    public static final TagKey<Item> SOUP_CONTAINER = itemTag("soup_container");
    public static final TagKey<Item> SPICE = itemTag("spice");

    //一般标识等
    public static final TagKey<Item> CUP = itemTag("cup");

    //forge通用
    public static final TagKey<Item> FORGE_AXES = forgeTag("tools/axes");
    public static final TagKey<Item> FORGE_SWORDS = forgeTag("tools/swords");
    public static final TagKey<Item> FORGE_KNIVES = forgeTag("tools/knives");
    public static final TagKey<Item> FORGE_CLEAVERS = forgeTag("tools/cleavers");
    public static final TagKey<Item> FORGE_DOUGH = forgeTag("dough");
    public static final TagKey<Item> FORGE_EGGS = forgeTag("eggs");
    public static final TagKey<Item> FORGE_TEA = forgeTag("tea");
    public static final TagKey<Item> FORGE_MILK = forgeTag("milk");

    public static final TagKey<Item> FD_KNIVES = fdTag("tools/knives");

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(IdyllicFoodDiary.MOD_ID, path));
    }

    private static TagKey<Item> forgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

    private static TagKey<Item> fdTag(String path) {
        return ItemTags.create(new ResourceLocation("farmersdelight", path));
    }

}
