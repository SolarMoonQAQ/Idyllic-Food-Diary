package cn.solarmoon.idyllic_food_diary.feature.hug_item

import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.client.IArmPoseTransformer

class HugArmPose(): IArmPoseTransformer {
    override fun applyTransform(
        model: HumanoidModel<*>,
        entity: LivingEntity,
        arm: HumanoidArm
    ) {
        model.leftArm.xRot = -(Math.PI / 5F).toFloat()
        model.leftArm.yRot = 0.0F
        model.rightArm.xRot = -(Math.PI / 5F).toFloat()
        model.rightArm.yRot = 0.0F
    }
}