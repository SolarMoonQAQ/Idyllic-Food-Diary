package cn.solarmoon.immersive_delight.api.common.item.specific;

import cn.solarmoon.immersive_delight.api.util.TextUtil;
import cn.solarmoon.immersive_delight.compat.create.Create;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 碗装食物类，可以绑定data的液体效果，也能放置对应方块
 * 默认16格堆叠
 * 需要自己给食物属性！
 */
public class AbstractBowlFoodItem extends BlockItem {

    public final String fluidBound;

    /**
     * @param fluidBound 绑定的液体对应效果，格式同minecraft:water
     */
    public AbstractBowlFoodItem(String fluidBound, Block block, Properties properties) {
        super(block, properties.craftRemainder(Items.BOWL).stacksTo(16));
        this.fluidBound = fluidBound;
    }

    public AbstractBowlFoodItem(Block block, Properties properties) {
        super(block, properties.craftRemainder(Items.BOWL).stacksTo(16));
        this.fluidBound = "empty";
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBound));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, 250);
            AbstractCupItem.commonUse(fluidStack, level, entity);
        }
        ItemStack itemStack = super.finishUsingItem(stack, level, entity);
        return entity instanceof Player && ((Player)entity).getAbilities().instabuild ? itemStack : new ItemStack(Items.BOWL);
    }

    /**
     * 显示药水信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        //data效果显示
        FluidEffect fluidEffect = FluidEffect.get(fluidBound);
        if(fluidEffect != null) {
            List<PotionEffect> potionEffects = fluidEffect.effects;
            if (potionEffects != null) {
                for (var effect : potionEffects) {
                    String name = effect.getEffect().getEffect().getDisplayName().getString();
                    int seconds = effect.duration;
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    String time = String.format("(%02d:%02d)", minutes, seconds);
                    int amplifier = effect.amplifier + 1;
                    String levelRoman = TextUtil.toRoman(amplifier);
                    Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
                    components.add(base);
                }
            }
            if(fluidEffect.clear) components.add(TextUtil.translation("tooltip", "fluid_effect_clear", ChatFormatting.BLUE));
            if(fluidEffect.extinguishing) components.add(TextUtil.translation("tooltip", "fluid_effect_extinguishing", ChatFormatting.BLUE));
            if(fluidEffect.fire > 0) components.add(TextUtil.translation("tooltip", "fluid_effect_fire", ChatFormatting.BLUE, fluidEffect.fire));
        }

        //Create药水效果显示
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBound));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, 250);
            String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";
            List<MobEffectInstance> effects = Create.getEffects(fluidTag);
            for (var effect : effects) {
                String name = effect.getEffect().getDisplayName().getString();
                int seconds = effect.getDuration();
                int minutes = seconds / 60;
                seconds = seconds % 60;
                String time = String.format("(%02d:%02d)", minutes, seconds);
                int amplifier = effect.getAmplifier() + 1;
                String levelRoman = TextUtil.toRoman(amplifier);
                Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
                components.add(base);
            }
        }
    }



}
