package cn.solarmoon.idyllic_food_diary.feature.hug_item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * 因为有些厨具具有双柄，为了增强沉浸感，为这些物品添加自定义的抱的动作，当然这里接入后只是实现了第三人称，第一人称需要修改IClientExtension
 */
public interface IHuggableItem {

    void transform3rdPDisplay(ItemStack stack, LivingEntity entity, ItemDisplayContext context, PoseStack poseStack);

}
