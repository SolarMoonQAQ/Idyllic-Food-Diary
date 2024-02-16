package cn.solarmoon.immersive_delight.compat.create.util;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.solarmoon_core.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class PotionUtil {

    /**
     * 机械动力联动：根据药水tag应用药水效果
     * @param fluidTag 液体tag的string
     * @param entity 玩家
     */
    public static void applyPotionFluidEffect(String fluidTag, LivingEntity entity, Level level) {
        if(fluidTag.contains("Potion")) {
            ResourceLocation potionId = new ResourceLocation(TextUtil.extractTag(fluidTag, "Potion"));
            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if(potion != null) {
                List<MobEffectInstance> effects = potion.getEffects();
                for (var effect : effects) {
                    if(!level.isClientSide) {
                        if (effect.getDuration() == 0)
                            entity.addEffect(new MobEffectInstance(effect.getEffect(), 1, effect.getAmplifier()));
                        else entity.addEffect(new MobEffectInstance(effect));
                    }
                    ImmersiveDelight.DEBUG.send("存在标签药水效果：" + effect.getEffect().getDisplayName().getString() + " " + effect.getAmplifier() + " " + effect.getDuration(), level);
                }
            }
        }
    }

    /**
     * 从药水tag获取药水
     */
    public static List<MobEffectInstance> getEffects(String fluidTag) {
        if(fluidTag.contains("Potion")) {
            ResourceLocation potionId = new ResourceLocation(TextUtil.extractTag(fluidTag, "Potion"));
            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);
            if (potion != null) {
                return potion.getEffects();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据液体显示其中tag所含有的药水效果到tooltip中
     */
    public static void addPotionHoverText(FluidStack fluidStack, List<Component> components) {
        String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";
        List<MobEffectInstance> effects = getEffects(fluidTag);
        for (var effect : effects) {
            String name = effect.getEffect().getDisplayName().getString();
            String time = TextUtil.toMinuteFormat(effect.getDuration(), true);
            int amplifier = effect.getAmplifier() + 1;
            String levelRoman = TextUtil.toRoman(amplifier);
            Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
            components.add(base);
        }
    }

}
