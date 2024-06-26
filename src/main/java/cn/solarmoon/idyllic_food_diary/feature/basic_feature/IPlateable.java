package cn.solarmoon.idyllic_food_diary.feature.basic_feature;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.util.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 可预存result和container的接口，适用于一些需要将合成结果预存在方块中再用容器取出的配方<br/>
 * 也是容器信息保存的桥梁<br/>
 * 在使用上，在类中给予空的result和container，在配方中找位置调用tryGiveResult/setContainer/setResult
 */
public interface IPlateable extends IExpGiver {

    String CONTAINER = "Container";
    String RESULT = "Result";

    /**
     * 将预存的结果给予玩家<br/>
     * 并且会将玩家手中盛放的容器信息存入食物
     */
    default boolean tryGiveResult(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        return tryGiveResult(player, heldItem);
    }

    /**
     * 将预存的结果给予玩家<br/>
     * 并且会将玩家手中盛放的容器信息存入食物
     */
    default boolean tryGiveResult(Player player, ItemStack heldItem) {
        if (hasResult()) {
            if (getContainer().test(heldItem)) {
                ItemStack result = getResult().split(1);
                ContainerHelper.setContainer(result, heldItem); // 这里保存了容器信息
                LevelSummonUtil.addItemToInventory(player, result);
                giveExp(player, true);
                if (!player.isCreative()) heldItem.shrink(1);
                resetContainer();
                ((BlockEntity)this).setChanged();
                return true;
            } else {
                Component message = IdyllicFoodDiary.TRANSLATOR.set("message", "container_required.empty");
                if (!getContainer().isEmpty()) {
                    message = IdyllicFoodDiary.TRANSLATOR.set("message", "container_required");
                }
                player.displayClientMessage(message, true);
            }
        }
        return false;
    }

    default void resetContainer() {
        setContainer(Ingredient.EMPTY);
    }

    /**
     * @return 预留槽是否被占用
     */
    default boolean hasResult() {
        return !getResult().isEmpty();
    }

    /**
     * 同时设置输出结果和匹配容器
     */
    default void setPending(ItemStack result, Ingredient container) {
        setResult(result);
        setContainer(container);
    }

    ItemStack getResult();

    Ingredient getContainer();

    void setResult(ItemStack newResult);

    void setContainer(Ingredient newContainer);

}
