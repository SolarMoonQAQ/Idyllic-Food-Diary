package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.Spice;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpiceList;
import cn.solarmoon.idyllic_food_diary.api.common.capability.serializable.SpicesData;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMCapabilities;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 接入该接口后，当拿着调味瓶右击接入该接口的方块时，将会为方块中的物品或是即将产出的产物添加spice属性，
 * 但是要注意，具体调用时机需要自己设置（在合适的地方调用addSpicesToItem方法）
 */
public interface ISpiceable extends IContainerBlockEntity {

    /**
     * 将调料全部加入输入的物品中
     * @param stack 要加调料的物品
     * @param addSpicesFormContainer 如果需要考虑之前的带调料的物品就填入这个集合，会把容器内物品的调料也加进来
     */
    default void addSpicesToItem(ItemStack stack, boolean addSpicesFormContainer) {
        if (addSpicesFormContainer) {
            for (ItemStack add : getStacks()) {
                SpicesData addData = CapabilityUtil.getData(add, IMCapabilities.FOOD_ITEM_DATA).getSpicesData();
                if (addData != null && !addData.isEmpty()) {
                    getSpices().addAll(addData.getSpices()); // 这里是把所有现存物品（不是预存物品）的所有调料混入待加的调料表
                }
            }
        }
        if (!getSpices().isEmpty()) {
            SpicesData spicesData = CapabilityUtil.getData(stack, IMCapabilities.FOOD_ITEM_DATA).getSpicesData();
            if (spicesData != null) {
                spicesData.getSpices().addAll(getSpices()); // 而这里是应用所有调料到指定物品上
            }
        }
        clearSpices();
    }

    /**
     * @param recipeNeed 配方所需要的所有调料
     * @param compareSpicesFromContainer 是否把容器内的物品加入匹配
     * @return 当前的调料待加集合中是否存在所需数量的调料
     */
    default boolean withTrueSpices(List<Spice> recipeNeed, boolean compareSpicesFromContainer) {
        return recipeNeed.stream()
                .allMatch(need -> getSpicesToAdd(compareSpicesFromContainer).stream()
                        .anyMatch(spice -> spice.isSame(need) && spice.getAmount() >= need.getAmount())
                );
    }

    /**
     * @return 返回一个完全独立的模拟的所有待加香料的表
     */
    default SpiceList getSpicesToAdd(boolean addSpicesFormContainer) {
        SpiceList spices = SpiceList.copyOf(getSpices());
        if (addSpicesFormContainer) {
            for (ItemStack add : getStacks()) {
                SpicesData addData = CapabilityUtil.getData(add, IMCapabilities.FOOD_ITEM_DATA).getSpicesData();
                if (addData != null && !addData.isEmpty()) {
                    spices.addAll(addData.getSpices());
                }
            }
        }
        return spices;
    }

    default void clearSpices() {
        getSpices().clear();
    }

    /**
     * 传入一个固定在private的空列表即可，然后不要修改此项！！
     */
    SpiceList getSpices();

    Spice.Step getSpiceStep();

    /**
     * @return 当它返回true时，会立刻清空待加的调料列表，并且会阻止调料罐放调料
     */
    boolean timeToResetSpices();

}
