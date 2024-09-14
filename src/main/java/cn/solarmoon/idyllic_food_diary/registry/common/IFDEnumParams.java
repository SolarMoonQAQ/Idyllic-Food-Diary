package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.element.matter.rolling_pin.RollingPinClientExtensions;
import cn.solarmoon.idyllic_food_diary.feature.hug_item.HugArmPose;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class IFDEnumParams {

    public static final EnumProxy<HumanoidModel.ArmPose> ROLLING = new EnumProxy<>(HumanoidModel.ArmPose.class, true, new RollingPinClientExtensions.RollingPose());

    public static final EnumProxy<HumanoidModel.ArmPose> HUG = new EnumProxy<>(HumanoidModel.ArmPose.class, true, new HugArmPose());

}
