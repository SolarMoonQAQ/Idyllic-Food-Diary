package cn.solarmoon.idyllic_food_diary.element.matter.cookware.fermentation_vat;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FermentationVatItem extends BlockItem implements ITankTileItem {

    public FermentationVatItem() {
        super(IMBlocks.FERMENTATION_VAT.get(), new Properties().stacksTo(1));
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new FermentationVatItemRenderer();
            }
        });
    }

}
