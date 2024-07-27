package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class MillstoneItem extends BlockItem {

    public MillstoneItem() {
        super(IMBlocks.MILLSTONE.get(), new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new MillstoneItemRenderer();
            }
        });
    }

}
