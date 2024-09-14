package cn.solarmoon.idyllic_food_diary.feature.hug_item

import cn.solarmoon.idyllic_food_diary.registry.common.IFDEnumParams
import cn.solarmoon.spark_core.api.renderer.IItemExtensionsWith3rdPTrans
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

interface IHuggableItemExtensions: IItemExtensionsWith3rdPTrans {

    override fun applyTransformTo3rdPerson(
        entity: LivingEntity,
        stack: ItemStack,
        context: ItemDisplayContext,
        arm: HumanoidArm,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        light: Int
    ) {
        if (entity.mainHandItem.isEmpty || entity.offhandItem.isEmpty) {
            if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
                poseStack.translate(0.365, 0.1, 0.3)
            } else if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
                poseStack.translate(-0.365, 0.1, 0.3)
            }
        }
    }

    override fun applyForgeHandTransform(
        poseStack: PoseStack,
        player: LocalPlayer,
        arm: HumanoidArm,
        itemInHand: ItemStack,
        partialTick: Float,
        equipProcess: Float,
        swingProcess: Float
    ): Boolean {
        if (player.mainHandItem.isEmpty || player.offhandItem.isEmpty) {
            poseStack.translate(if (arm == HumanoidArm.RIGHT) -0.5 else 0.5, 0.0, 0.1)
            return false
        }
        return false
    }

    override fun getArmPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose? {
        if (entityLiving.mainHandItem.isEmpty || entityLiving.offhandItem.isEmpty) {
            return IFDEnumParams.HUG.value
        }
        return null
    }

}