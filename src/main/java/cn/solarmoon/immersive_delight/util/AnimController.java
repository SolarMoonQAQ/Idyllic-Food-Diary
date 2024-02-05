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
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Objects;


@SuppressWarnings("unchecked")
public class AnimController {

    private AbstractClientPlayer player;
    private ModifierLayer<IAnimation> animation;


    public AnimController(Entity entity) {
        if (entity instanceof AbstractClientPlayer clientPlayer) {
            this.player = clientPlayer;
            this.animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(new ResourceLocation(ImmersiveDelight.MOD_ID, "animation"));
        }
    }

    public ModifierLayer<IAnimation> getAnimation() {
        return animation;
    }

    public boolean isActive() {
        if (animation != null) {
            return animation.isActive();
        }
        return false;
    }

    public void playAnim(int length, String anim) {
        if (player == null || animation == null) return;
        if (animation.isActive()) return;
        animation.replaceAnimationWithFade(AbstractFadeModifier.functionalFadeIn(length, (modelName, type, value) -> value),
                new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(ImmersiveDelight.MOD_ID, anim))))
                        .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL));
    }

    public void stopAnim(int length) {
        if (player == null || animation == null) return;
        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(length, Ease.INOUTCUBIC), null);
    }

}
