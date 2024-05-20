package cn.solarmoon.idyllic_food_diary.core.client.model.item;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

/**
 * 带容器食物的item渲染，将会把盘子一类的容器渲染在食物下层，具体实现在 {@link ContainableFoodItemOverride} 中
 */
public class ContainableFoodBakedModel extends BakedModelWrapper<BakedModel> {

    public ContainableFoodBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ContainableFoodItemOverride();
    }

}

