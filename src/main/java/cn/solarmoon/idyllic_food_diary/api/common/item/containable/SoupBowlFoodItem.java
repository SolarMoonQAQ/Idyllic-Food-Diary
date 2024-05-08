package cn.solarmoon.idyllic_food_diary.api.common.item.containable;

import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.idyllic_food_diary.core.compat.create.util.PotionUtil;
import cn.solarmoon.idyllic_food_diary.core.data.fluid_effects.serializer.FluidEffect;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
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
public class SoupBowlFoodItem extends BlockItem {

    public final String fluidBound;

    /**
     * @param fluidBound 绑定的液体对应效果，格式同minecraft:water（无需食物效果，在数据包中修改）
     */
    public SoupBowlFoodItem(String fluidBound, Block block) {
        super(block, new Properties().food(
                new FoodProperties.Builder().build()
        ).stacksTo(16).craftRemainder(Items.BOWL));
        this.fluidBound = fluidBound;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        applyFluidEffect(level, entity);
        if (entity instanceof Player player && !player.isCreative()) {
            stack.shrink(1); //这里不用return来削减stack因为会在刚好背包满时在食用完刚好有一个空位的情况下会把物品丢出去
            LevelSummonUtil.addItemToInventory(player, new ItemStack(Items.BOWL));
        }
        return stack;
    }

    /**
     * 应用液体效果
     */
    public void applyFluidEffect(Level level, LivingEntity entity) { //此方法未来可能改为通用
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBound));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, 250);
            FarmerUtil.commonDrink(fluidStack, level, entity, true);
        }
    }

    /**
     * 显示药水信息
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        //data效果显示
        FluidEffect fluidEffect = FluidEffect.get(fluidBound);
        FarmerUtil.showFluidEffectTooltip(fluidEffect, components);

        //Create药水效果显示
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidBound));
        if (fluid != null) {
            FluidStack fluidStack = new FluidStack(fluid, 250);
            PotionUtil.addPotionHoverText(fluidStack, components);
        }
    }



}
