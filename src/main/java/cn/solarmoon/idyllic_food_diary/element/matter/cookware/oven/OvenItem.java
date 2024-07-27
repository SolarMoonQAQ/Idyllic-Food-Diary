package cn.solarmoon.idyllic_food_diary.element.matter.cookware.oven;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class OvenItem extends BlockItem {

    public OvenItem() {
        super(IMBlocks.OVEN.get(), new Properties().stacksTo(1));
    }

}
