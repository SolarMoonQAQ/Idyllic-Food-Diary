package cn.solarmoon.immersive_delight.data.tags;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.IMItems;
import cn.solarmoon.immersive_delight.compat.farmersdelight.FarmersDelight;
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
        tag(IMItemTags.FORGE_ROLLING_DOUGH).add(
                FarmersDelight.WHEAT_DOUGH.get(),
                IMItems.FLATBREAD_DOUGH.get()
        );
    }

    public static final TagKey<Item> FORGE_ROLLING_DOUGH = itemForgeTag("rolling/dough");

    private static TagKey<Item> itemTag(String path) {
        return ItemTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, path));
    }

    private static TagKey<Item> itemForgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

}
