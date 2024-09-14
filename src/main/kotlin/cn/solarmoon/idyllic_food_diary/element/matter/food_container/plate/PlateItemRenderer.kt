package cn.solarmoon.idyllic_food_diary.element.matter.food_container.plate

import cn.solarmoon.idyllic_food_diary.feature.util.RenderUtil
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import com.mojang.authlib.minecraft.client.MinecraftClient
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class PlateItemRenderer: HandyItemRenderer() {
    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        blockRenderer.renderSingleBlock(Block.byItem(stack.item).defaultBlockState(), poseStack, buffer, packedLight, packedOverlay)
        val ac = Minecraft.getInstance().level?.registryAccess() ?: return
        RenderUtil.renderItemStackStack(ac, 0.5f, 45f, 2f, 0.5f, stack, poseStack, buffer, packedLight, packedOverlay)
    }
}