package cn.solarmoon.idyllic_food_diary.element.matter.stove.water_storage_stove;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class WaterStorageStoveItem extends BlockItem implements ITankTileItem {

    public WaterStorageStoveItem() {
        super(IMBlocks.WATER_STORAGE_STOVE.get(), new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WaterStorageStoveItemRenderer();
            }
        });
    }

    @Override
    public int getMaxCapacity() {
        return 1000;
    }

}
