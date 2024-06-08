package cn.solarmoon.idyllic_food_diary.feature.logic.water_pouring;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupItem;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.AbstractKettleItem;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.idyllic_food_diary.util.namespace.NETList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * shift+左键把水倒空
 */
public class DrinkingClientEvent {

    /**
     * 可以把水泼出去并对范围内的实体产生效果
     */
    @SubscribeEvent
    public void pour(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        if(stack.getItem() instanceof AbstractCupItem
                || stack.getItem() instanceof AbstractKettleItem
                || stack.getItem() instanceof CookingPotItem
        ) {
            if(event.getEntity().isCrouching()) {
                IMPacks.SERVER_PACK.getSender().pos(pos).stack(stack).send(NETList.POURING);
            }
        }
    }

}
