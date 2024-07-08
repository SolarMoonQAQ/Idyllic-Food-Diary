package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.inventory.TileItemContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ContainerHelper {

    public static final String FOOD_CONTAINER = "FoodContainer";

    /**
     * @return 获取该物品身上的食物容器信息
     */
    public static ItemStack getContainer(ItemStack origin) {
        CompoundTag tag = origin.getTag();
        if (tag != null && tag.contains(FOOD_CONTAINER)) {
            return ItemStack.of(tag.getCompound(FOOD_CONTAINER));
        }
        return ItemStack.EMPTY;
    }

    /**
     * 保存食物容器信息到origin身上，要求设置的容器不为空且必须是已被tag标记的专门容器，不然像荷叶这样的内容就不好搞定
     */
    public static void setContainer(ItemStack origin, ItemStack containerSet) {
        if (isFoodContainer(containerSet)) {
            if (!containerSet.isEmpty()) {
                CompoundTag tag = origin.getOrCreateTag();
                tag.put(FOOD_CONTAINER, containerSet.serializeNBT()); // 此处把容器信息存入了物品
            }
        }
    }

    /**
     * 保存食物容器信息到origin身上，要求设置的容器不为空且必须是已被tag标记的专门容器，不然像荷叶这样的内容就不好搞定
     */
    public static void setContainer(CompoundTag tag, ItemStack containerSet) {
        if (isFoodContainer(containerSet)) {
            if (!containerSet.isEmpty()) {
                tag.put(FOOD_CONTAINER, containerSet.serializeNBT()); // 此处把容器信息存入了物品
            }
        }
    }

    public static boolean isFoodContainer(ItemStack itemStack) {
        return itemStack.is(IMItemTags.SMALL_CONTAINER) || itemStack.is(IMItemTags.MEDIUM_CONTAINER) || itemStack.is(IMItemTags.LARGE_CONTAINER);
    }

    /**
     * 所有涉及到容器盛取的都必须用这个方法判断
     * @param container 给定的容器材料组
     * @param heldItem 手持物品
     * @return 手持物品是否匹配材料组，并且手持容器内没有其他物品
     */
    public static boolean test(Ingredient container, ItemStack heldItem) {
        var op = TileItemContainerHelper.getInventory(heldItem);
        boolean invEmpty = op.isPresent() && ItemHandlerUtil.getStacks(op.get()).isEmpty(); // 保证餐盘都是空的才能装
        return container.test(heldItem) && invEmpty;
    }

}
