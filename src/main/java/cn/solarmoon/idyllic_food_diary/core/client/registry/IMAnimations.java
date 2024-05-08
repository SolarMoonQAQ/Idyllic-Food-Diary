package cn.solarmoon.idyllic_food_diary.core.client.registry;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary.MOD_ID;


@Mod.EventBusSubscriber(modid = IdyllicFoodDiary.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IMAnimations {

    @SubscribeEvent
    public static void animationRegister(final FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new ResourceLocation(MOD_ID, "animation"), 42, (player) -> {
            if (player instanceof LocalPlayer) {
                ModifierLayer<IAnimation> animation =  new ModifierLayer<>();
                animation.addModifierBefore(new SpeedModifier(1.0f)); // 设置动画速度
                animation.addModifierBefore(new MirrorModifier(false)); // 设置动画镜像
                return animation;
            }
            return null;
        });
    }

}
