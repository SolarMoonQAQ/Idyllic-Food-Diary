package cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class GrillItem extends BlockItem {

    public GrillItem() {
        super(IMBlocks.GRILL.get(), new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new GrillItemRenderer();
            }
        });
    }

}
