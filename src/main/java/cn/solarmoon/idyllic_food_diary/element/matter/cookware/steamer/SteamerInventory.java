package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer;

import cn.solarmoon.solarmoon_core.api.tile.inventory.TileInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SteamerInventory extends TileInventory {

    public SteamerInventory(int size, SteamerBlockEntity steamer) {
        super(size, 1, steamer);
    }

    /**
     * @return 删除所有非空元素的新stacks
     */
    public NonNullList<ItemStack> getStacks() {
        return stacks;
    }

}
