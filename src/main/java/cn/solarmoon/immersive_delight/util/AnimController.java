package cn.solarmoon.immersive_delight.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

import static cn.solarmoon.immersive_delight.util.Constants.mc;


@SuppressWarnings("unchecked")
public class AnimController {

    public void playAnim(int length , String anim) {
        if(mc.player == null) return;
        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(mc.player).get(new ResourceLocation(ImmersiveDelight.MOD_ID, "animation"));
        if(animation == null) return;
        if(animation.isActive()) return;
        animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(length, (modelName, type, value) -> value),
                new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ImmersiveDelight.MOD_ID, anim))))
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
    }

    public void stopAnim(int length) {
        if(mc.player == null) return;
        ModifierLayer<IAnimation> animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(mc.player).get(new ResourceLocation(ImmersiveDelight.MOD_ID, "animation"));
        if(animation == null) return;
        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(length, Ease.INOUTCUBIC), null);
    }

}
