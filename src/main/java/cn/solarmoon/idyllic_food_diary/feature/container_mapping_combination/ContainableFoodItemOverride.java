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

/**
 * 根据是否有容器选择是否贴入容器的贴图
 */
public class ContainableFoodItemOverride extends ItemOverrides {

    @Nullable
    @Override
    public BakedModel resolve(BakedModel bakedModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
        BakedModel origin = super.resolve(bakedModel, stack, level, entity, i);
        ItemStack container = ContainerHelper.getContainer(stack);
        if (!container.isEmpty()) {
            BakedModel containerModel = Minecraft.getInstance().getItemRenderer()
                    .getModel(AbstractContainerItem.copyForPureMapping(container), level, entity, i);
            return new CompositeBakedModel(origin, containerModel);
        }
        return origin;
    }

}
