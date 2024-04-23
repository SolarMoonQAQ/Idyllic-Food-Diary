package cn.solarmoon.idyllic_food_diary.common.registry.ability;

import cn.solarmoon.idyllic_food_diary.common.registry.IMBlocks;
import cn.solarmoon.solarmoon_core.api.registry.base.BasePlaceableItemRegistry;
import net.minecraft.world.item.Items;

public class IMPlaceableItems extends BasePlaceableItemRegistry {
    @Override
    public void addRegistry() {
        put(Items.MUSHROOM_STEW, IMBlocks.MUSHROOM_STEW.get());
        put(Items.BEETROOT_SOUP, IMBlocks.BEETROOT_SOUP.get());
        put(Items.BOWL, IMBlocks.BOWL.get());
    }
}
