package cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer

import cn.solarmoon.idyllic_food_diary.registry.common.IFDBlocks
import cn.solarmoon.idyllic_food_diary.registry.common.IFDDataComponents
import cn.solarmoon.spark_core.api.renderer.HandyItemRenderer
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.nbt.ListTag
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class SteamerItemRenderer: HandyItemRenderer() {
    override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val level = Minecraft.getInstance().level ?: return
        val content = SteamerItem.getInvListTag(stack, level.registryAccess())
        val stackAmount = content.size
        val state = IFDBlocks.STEAMER.get().defaultBlockState()

        Minecraft.getInstance().blockRenderer.renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);

        // 渲染每层蒸笼
        for (i in 0 until stackAmount) {
            val h = 4 / 16.0 * i;
            poseStack.pushPose();
            poseStack.translate(0.0, h, 0.0);
            Minecraft.getInstance().blockRenderer.renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }

        // 渲染盖子
        if (!SteamerItem.getLid(stack, level.registryAccess()).isEmpty) {
            val h = 4 / 16.0 * stackAmount;
            poseStack.pushPose();
            poseStack.translate(0.0, h, 0.0);
            Minecraft.getInstance().blockRenderer.renderSingleBlock(IFDBlocks.STEAMER_LID.get().defaultBlockState(), poseStack, buffer, packedLight, packedOverlay);
            poseStack.popPose();
        }
    }
}