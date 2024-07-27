package cn.solarmoon.idyllic_food_diary.feature.container_mapping_combination;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.container.AbstractContainerItem;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.item_model.CompositeBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ContainerItemOverride extends ItemOverrides {

    private final BakedModel pureModel;

    public ContainerItemOverride(BakedModel pureModel) {
        this.pureModel = pureModel;
    }

    @Nullable
    @Override
    public BakedModel resolve(BakedModel bakedModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
        BakedModel origin = super.resolve(bakedModel, stack, level, entity, i);
        if (AbstractContainerItem.getIfShouldRenderPurely(stack) == 1) return pureModel;
        return origin;
    }

}
