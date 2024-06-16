package cn.solarmoon.idyllic_food_diary.feature.spit_item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 吐子儿
 */
public class SpittableItem {

    public final static List<SpittableItem> ALL = new ArrayList<>();

    private final Item itemBound;
    private final ItemStack spit;

    public SpittableItem(Item itemBound, ItemStack spit) {
        this.itemBound = itemBound;
        this.spit = spit;
    }

    public boolean isItemEqual(ItemStack stack) {
        return stack.is(itemBound);
    }

    /**
     * 吐子
     * @param player 吐子的玩家
     */
    public void doSpit(Player player, ItemStack stack) {
        if (isItemEqual(stack)) {
            SpitUtil.spit(spit, player);
        }
    }

}
