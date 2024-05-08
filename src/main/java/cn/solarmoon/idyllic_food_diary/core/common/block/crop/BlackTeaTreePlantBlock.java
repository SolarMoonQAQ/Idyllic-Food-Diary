package cn.solarmoon.idyllic_food_diary.core.common.block.crop;

import cn.solarmoon.idyllic_food_diary.api.common.block.crop.AbstractTeaPlantCropBlock;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMItems;
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
