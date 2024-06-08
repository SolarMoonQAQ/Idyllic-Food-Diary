package cn.solarmoon.idyllic_food_diary.element.matter.crop;

import cn.solarmoon.idyllic_food_diary.registry.common.IMItems;
import net.minecraft.world.item.Item;

public class BlackTeaTreePlantBlock extends AbstractTeaPlantCropBlock {

    public BlackTeaTreePlantBlock() {

    }

    /**
     * 产物为红茶叶
     */
    @Override
    public Item getHarvestItem() {
        return IMItems.BLACK_TEA_LEAF.get();
    }

}
