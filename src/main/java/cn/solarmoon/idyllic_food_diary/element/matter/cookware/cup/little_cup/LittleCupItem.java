package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class LittleCupItem extends AbstractCupItem {

    public LittleCupItem(Block block) {
        super(block, new Properties());
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new LittleCupItemRenderer();
            }
        });
    }

}
