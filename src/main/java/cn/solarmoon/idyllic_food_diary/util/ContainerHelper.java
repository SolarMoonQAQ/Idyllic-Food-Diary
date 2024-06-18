package cn.solarmoon.idyllic_food_diary.util;

import cn.solarmoon.idyllic_food_diary.data.IMItemTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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

}
