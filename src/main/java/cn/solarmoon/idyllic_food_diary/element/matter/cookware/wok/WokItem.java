package cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlocks;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTileItem;
import cn.solarmoon.solarmoon_core.api.renderer.BaseItemRenderer;
import cn.solarmoon.solarmoon_core.api.renderer.IItemRendererProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IArmPoseTransformer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class WokItem extends BlockItem implements IItemRendererProvider, ITankTileItem {

    public WokItem() {
        super(IMBlocks.WOK.get(), new Properties().stacksTo(1));
    }

    @Override
    public Supplier<BaseItemRenderer> getRendererFactory() {
        return WokItemRenderer::new;
    }

    @Override
    public int getMaxCapacity() {
        return 250;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {

            public static final HumanoidModel.ArmPose pose = HumanoidModel.ArmPose.create("HUG", true, (model, entity, arm) -> {
                model.leftArm.xRot = -((float) Math.PI / 5F);
                model.leftArm.yRot = 0.0F;
                model.rightArm.xRot = -((float) Math.PI / 5F);
                model.rightArm.yRot = 0.0F;
            });

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getMainHandItem().isEmpty() || entityLiving.getOffhandItem().isEmpty()) {
                    return pose;
                }
                return IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new WokItemRenderer();
            }

        });
    }
}
