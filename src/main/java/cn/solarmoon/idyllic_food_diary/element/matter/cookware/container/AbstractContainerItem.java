package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.long_plate.LongPlateItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractContainerItem extends BlockItem {

    public AbstractContainerItem(Block block) {
        super(block, new Properties().stacksTo(16));
    }

    @OnlyIn(Dist.CLIENT)
    public abstract Supplier<BaseItemRenderer> getItemRenderer();

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return getItemRenderer().get();
            }
        });
    }

    public static final String PURE_MAPPING = "PureMapping";

    /**
     * @return 盘子和菜贴图耦合用，强行给模型一个使用纯贴图的copy版本的容器贴图
     */
    public static ItemStack copyForPureMapping(ItemStack container) {
        ItemStack copy = container.copy();
        copy.getOrCreateTag().putInt(PURE_MAPPING, 1);
        return copy;
    }

    /**
     * @return copy一下看是否使用纯贴图，不改变物品本身
     */
    public static float getIfShouldRenderPurely(ItemStack container) {
        return container.copy().getOrCreateTag().getInt(PURE_MAPPING);
    }

}
