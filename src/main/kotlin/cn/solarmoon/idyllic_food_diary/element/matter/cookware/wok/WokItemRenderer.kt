package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok

import cn.solarmoon.idyllic_food_diary.feature.util.RenderUtil
import cn.solarmoon.spark_core.api.attachment.animation.AnimHelper
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.capabilities.Capabilities

class WokItemRenderer: HandyItemRenderer() {
    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        blockRenderer.renderSingleBlock(Block.byItem(stack.item).defaultBlockState(), poseStack, buffer, packedLight, packedOverlay)

        // 液体
        stack.getCapability(Capabilities.FluidHandler.ITEM)?.let { tank ->
            AnimHelper.Fluid.renderStaticFluid(10/16f, 3/16f, 1/16f, tank, poseStack, buffer, packedLight)
        }

        Minecraft.getInstance().level?.let {
            RenderUtil.renderItemStackStack(it.registryAccess(), 0.4f, 60f, 1f, 0.5f, stack, poseStack, buffer, packedLight, packedOverlay)
        }
    }
}