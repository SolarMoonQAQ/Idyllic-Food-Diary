// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
package cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone;


import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.api.IBlockEntityAnimation;
import cn.solarmoon.idyllic_food_diary.api.Timer;
import cn.solarmoon.idyllic_food_diary.registry.client.IMLayers;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.renderer.BaseBlockEntityRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.TextureRenderUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

public class MillstoneBlockRenderer extends BaseBlockEntityRenderer<MillstoneBlockEntity> {

	private final ModelPart wheel;
	private final ModelPart main;

	public MillstoneBlockRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
		wheel = context.bakeLayer(IMLayers.MILLSTONE.get()).getChild("wheel");
		wheel.setRotation(0, 0, (float) -Math.PI);
		main = context.bakeLayer(IMLayers.MILLSTONE.get()).getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition wheel = partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(61, 5).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(56, 53).addBox(5.0F, -1.0F, 0.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 34).addBox(7.0F, 0.0F, 0.0F, 1.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 53).addBox(1.0F, 0.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(40, 53).addBox(1.0F, 0.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(48, 53).addBox(-2.0F, 0.0F, -3.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(34, 34).addBox(-7.0F, 0.0F, 15.0F, 14.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(40, 15).addBox(-8.0F, 0.0F, 0.0F, 1.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-7.0F, 0.0F, 1.0F, 14.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(14, 53).addBox(-7.0F, 0.0F, 0.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(28, 53).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 15).addBox(-5.0F, 1.0F, 3.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void render(MillstoneBlockEntity millstone, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(new ResourceLocation(IdyllicFoodDiary.MOD_ID, "textures/block/millstone.png")));
		Level level = millstone.getLevel();
		if (level == null) return;
		Direction direction = millstone.getBlockState().getValue(IHorizontalFacingBlock.FACING);
		light = LevelRenderer.getLightColor(millstone.getLevel(), millstone.getBlockPos());

		poseStack.translate(0.5f, 0, 0.5f);
		poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot() + 180));
		poseStack.pushPose();
		AnimTicker anim = ((IBlockEntityAnimation)millstone).getAnimTickers().get("rotation");
		Timer timer = anim.getTimer();
		var values = anim.getFixedValues();
		if (timer.getTiming()) {
			poseStack.mulPose(Axis.YP.rotationDegrees(values.getOrDefault("rot", 0f) + partialTicks * values.getOrDefault("velocity", 0f)));
		} else {
			poseStack.mulPose(Axis.YP.rotationDegrees(values.getOrDefault("rot", 0f)));
		}
		wheel.render(poseStack, vertexConsumer, light, overlay);
		poseStack.popPose();

		poseStack.pushPose();
		main.render(poseStack, vertexConsumer, light, overlay);
		poseStack.popPose();

		renderAnimatedFluid(millstone, Direction.DOWN, partialTicks, poseStack, buffer, light);
	}

	public static void renderAnimatedFluid(MillstoneBlockEntity mill, Direction side, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
		mill.getCapability(ForgeCapabilities.FLUID_HANDLER, side).ifPresent(tank -> {
			AnimTicker anim = AnimHelper.getMap(mill).get("flow");
			FluidStack fluidStack = tank.getFluidInTank(0);
			int targetColor = TextureRenderUtil.getColor(fluidStack);
			IClientFluidTypeExtensions fluidAttributes = IClientFluidTypeExtensions.of(fluidStack.getFluid());
			ResourceLocation spriteLocation = fluidAttributes.getStillTexture(fluidStack);

			if (spriteLocation != null) {
				// 盘中液体
				poseStack.pushPose();
				poseStack.translate(-0.5, 0, -0.5);
				AnimHelper.Fluid.renderAnimatedFluid(mill, side, 14 / 16f, (2 - 0.02f) / 16f, 1 / 16f, partialTicks, poseStack, buffer, light);
				poseStack.popPose();
				poseStack.pushPose();
				poseStack.translate(-0.5, 0, -1);
				AnimHelper.Fluid.renderAnimatedFluid(mill, side, 2 / 16f, 2 / 16f, 1 / 16F, partialTicks, poseStack, buffer, light);
				poseStack.translate(0, 0, -2/16f);
				AnimHelper.Fluid.renderAnimatedFluid(mill, side, 2 / 16f, 2 / 16f, 1 / 16F, partialTicks, poseStack, buffer, light);
				poseStack.popPose();

				// 龙头
				float tick = anim.getFixedValues().getOrDefault("flow", 0f);
				float maxTick = anim.getFixedValues().getOrDefault("flowMax", 10f);
				float delta = partialTicks;
				if (tick > 0) {
					float h = 2 * FluidHandlerUtil.getScale(tank);
					if (tick >= maxTick) delta = 0;
					if (!mill.isFlowing()) delta = -partialTicks;
					poseStack.pushPose();
					poseStack.translate(-0.5, (1 + h) / 16f, -4 / 16f);
					poseStack.mulPose(Axis.XN.rotationDegrees(180));
					TextureRenderUtil.render(spriteLocation, 0, 0, 2, (int) (16 * (tick / maxTick)), 2 / 16F, (tick + delta) * (16 + h) / 16f / maxTick, targetColor, 1.0F, 0, poseStack, buffer, light);
					poseStack.popPose();
				}
			}

		});
	}

	@Override
	public boolean shouldRender(MillstoneBlockEntity p_173568_, Vec3 p_173569_) {
		return true;
	}

	@Override
	public boolean shouldRenderOffScreen(MillstoneBlockEntity p_112306_) {
		return true;
	}

}