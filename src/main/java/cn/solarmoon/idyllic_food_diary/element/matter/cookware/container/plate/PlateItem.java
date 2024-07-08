package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.plate;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class PlateItem extends AbstractContainerItem {

    public PlateItem(Block block) {
        super(block);
    }

    @Override
    public BaseItemRenderer getItemRenderer() {
        return new PlateItemRenderer();
    }

}
