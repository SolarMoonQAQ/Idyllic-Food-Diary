package cn.solarmoon.immersive_delight.common.item.base;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.compat.create.util.PotionUtil;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.immersive_delight.data.fluid_effects.serializer.PotionEffect;
import cn.solarmoon.solarmoon_core.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
 * 碗装食物类，可以绑定data的液体效果，也能放置对应方块<br/>
 * 默认16格堆叠<br/>
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
                    String time = TextUtil.toMinuteFormat(effect.duration, true);
                    int amplifier = effect.amplifier + 1;
                    String levelRoman = TextUtil.toRoman(amplifier);
                    Component base = Component.literal(name + " " + levelRoman + " " + time).withStyle(ChatFormatting.BLUE);
                    components.add(base);
                }
            }
            if(fluidEffect.clear) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_clear", ChatFormatting.BLUE));
            if(fluidEffect.extinguishing) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_extinguishing", ChatFormatting.BLUE));
            if(fluidEffect.fire > 0) components.add(ImmersiveDelight.TRANSLATOR.set("tooltip", "fluid_effect_fire", ChatFormatting.BLUE, fluidEffect.fire));
        }

        //Create药水效果显示
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBound));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, 250);
            PotionUtil.addPotionHoverText(fluidStack, components);
        }
    }



}
