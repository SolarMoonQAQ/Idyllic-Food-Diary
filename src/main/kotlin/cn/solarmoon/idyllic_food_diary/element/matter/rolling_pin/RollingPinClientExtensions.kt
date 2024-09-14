package cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin

import cn.solarmoon.idyllic_food_diary.registry.common.IFDEnumParams
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.IArmPoseTransformer
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import kotlin.math.abs

class RollingPinClientExtensions: IClientItemExtensions {

    override fun getArmPose(
        entityLiving: LivingEntity,
        hand: InteractionHand,
        itemStack: ItemStack
    ): HumanoidModel.ArmPose? {
        if (entityLiving.useItem.`is`(itemStack.item)) return IFDEnumParams.ROLLING.value
        return null
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
        if (player.getUseItem().`is`(itemInHand.item)) {
            val time = 20 - abs((player.useItemRemainingTicks - partialTick) % 40 - 20)
            poseStack.translate(0.1f, 0.2f, -time / 20f)
            poseStack.mulPose(Axis.ZN.rotationDegrees(102.5f))
            poseStack.mulPose(Axis.XP.rotationDegrees(25f))
        }
        return super.applyForgeHandTransform(poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess)
    }

    class RollingPose: IArmPoseTransformer {
        override fun applyTransform(
            model: HumanoidModel<*>,
            entity: LivingEntity,
            arm: HumanoidArm
        ) {
            val time = 20 - abs((entity.useItemRemainingTicks - Minecraft.getInstance().timer.getGameTimeDeltaPartialTick(true)) % 40 - 20)
            val timeInRadians = Math.toRadians(time.toDouble())
            model.leftArm.yRot = (-Math.PI / 10F + timeInRadians).toFloat()
            model.leftArm.xRot = (-Math.PI * 100 / 180).toFloat()
            model.leftArm.zRot = (Math.PI / 2).toFloat()
            model.rightArm.yRot = (-Math.PI / 10F + timeInRadians).toFloat()
            model.rightArm.xRot = (-Math.PI * 80 / 180).toFloat()
            model.rightArm.zRot = (Math.PI / 2).toFloat()
        }
    }

}