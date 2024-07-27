package cn.solarmoon.idyllic_food_diary.feature.container_mapping_combination;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

public class ContainerBakedModel extends BakedModelWrapper<BakedModel> {

    private final BakedModel pureModel;

    public ContainerBakedModel(BakedModel originalModel, BakedModel pureModel) {
        super(originalModel);
        this.pureModel = pureModel;
    }

    @Override
    public ItemOverrides getOverrides() {
        return new ContainerItemOverride(pureModel);
    }

}
