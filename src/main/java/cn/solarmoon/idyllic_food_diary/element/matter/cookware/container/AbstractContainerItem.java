package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractContainerItem extends BlockItem {

    public AbstractContainerItem(Block block) {
        super(block, new Properties().stacksTo(16));
    }

    public abstract BaseItemRenderer getItemRenderer();

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return getItemRenderer();
            }
        });
    }

}
