package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup

import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.Capabilities

class LittleCupItemRenderer: HandyItemRenderer() {
    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        Minecraft.getInstance().blockRenderer.renderSingleBlock(Block.byItem(stack.item).defaultBlockState(), poseStack, buffer, packedLight, packedOverlay)
        val tank = stack.getCapability(Capabilities.FluidHandler.ITEM) ?: return
        AnimHelper.Fluid.renderStaticFluid(2/16f, 4/16f, 1/16f, tank, poseStack, buffer, packedLight)
    }
}