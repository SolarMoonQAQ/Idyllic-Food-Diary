package cn.solarmoon.idyllic_food_diary.core.client.humanoid_model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ConjurerHatModel<T extends LivingEntity> extends HumanoidModel<T>
{
    protected final ModelPart hatTop;
    protected final ModelPart hatBottom;

    public ConjurerHatModel(ModelPart model)
    {
        super(model);

        this.hatTop = model.getChild("hat_top");
        this.hatBottom = model.getChild("hat_bottom");
    }

    public static LayerDefinition createLayer()
    {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("hat_top", CubeListBuilder.create().texOffs(96, 0).addBox(-4.0F, -15.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.01F)), PartPose.ZERO);
        root.addOrReplaceChild("hat_bottom", CubeListBuilder.create().texOffs(84, 16).addBox(-5.5F, -7.0F, -5.5F, 11, 2, 11, new CubeDeformation(0.01F)), PartPose.ZERO);

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    protected Iterable<ModelPart> headParts()
    {
        float offset = this.head.y;

        this.hatTop.copyFrom(this.head);
        this.hatBottom.copyFrom(this.head);

        this.hatTop.y = offset;
        this.hatBottom.y = offset;

        return List.of(this.hatBottom, this.hatTop);
    }

    @Override
    protected Iterable<ModelPart> bodyParts()
    {
        return List.of();
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		/*if (!(entityIn instanceof ArmorStandEntity))
		{
			float offset = this.bipedHead.rotationPointY;

			this.hattop.copyModelAngles(this.bipedHead);
			this.hatbottom.copyModelAngles(this.bipedHead);

			this.hattop.rotationPointY = offset;
			this.hatbottom.rotationPointY = offset;
		}*/

    }

    public void setRotateAngle(ModelPart ModelRenderer, float x, float y, float z)
    {
        ModelRenderer.xRot = x;
        ModelRenderer.yRot = y;
        ModelRenderer.zRot = z;
    }
}


