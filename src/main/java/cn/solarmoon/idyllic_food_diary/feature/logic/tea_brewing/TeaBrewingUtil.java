package cn.solarmoon.idyllic_food_diary.feature.logic.tea_brewing;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.logic.generic_recipe.soup_serving.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.data.serializable.PotionEffect;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeaBrewingUtil {

    /**
     * 用于mixin，根据茶内容物组合茶名字
     */
    public static void resetFluidName(FluidStack fluidStack, Component description, CallbackInfoReturnable<Component> cir) {
        if (!fluidStack.isEmpty()) {
            CompoundTag nameTag = fluidStack.getOrCreateTag().getCompound(TeaIngredient.CUSTOM_NAME);
            String side = nameTag.getString(TeaIngredient.Type.SIDE.toString());
            String add = nameTag.getString(TeaIngredient.Type.ADD.toString());
            Component sideC = side.isEmpty() ? Component.empty() : Component.translatable(side);
            Component addC = add.isEmpty() ? Component.empty() : Component.translatable(add);
            cir.setReturnValue(sideC.copy().append(addC).append(description));
        }
    }

    public static void commonDrink(FluidStack fluidStack, LivingEntity entity, boolean needFood) {
        Level level = entity.level();
        List<TeaIngredient> teaIngredients = TeaIngredient.readFromFluidStack(fluidStack);
        // 此处先应用需要累计的效果
        boolean clear = false;
        int fire = 0;
        int extinguishValue = 0;
        for (var ti : teaIngredients) {
            fire = fire + ti.getFireTime();
            if (ti.canClearAllEffect()) clear = true;
            extinguishValue = ti.canExtinguishing() ? extinguishValue + 1 : extinguishValue - 1;
        }
        // 应用着火效果（value为0也是什么也不做（中和了））  - 举例 ： 一个着火(fire>0)，一个灭火(extinguish)，那么中和起来就不会着火也不会灭火
        if (extinguishValue == 0) fire = 0;
        entity.setSecondsOnFire(fire);
        // 应用清醒效果 （有一个清醒就可以清醒）
        // 注意这里必须首先清醒，不然这里的药水效果都白给
        if (clear) {
            entity.removeAllEffects();
        }
        // 应用灭火效果 <- 先着火再灭火，这样如果有着火和灭火的搭配起来就可以不着火
        if (extinguishValue > 0) {
            entity.clearFire();
        }

        // 此处应用无需累计的效果
        for (TeaIngredient teaIngredient : teaIngredients) {
            // 应用药水效果
            List<PotionEffect> potionEffects = teaIngredient.getEffects();
            if (potionEffects != null) {
                for (PotionEffect potionEffect : potionEffects) {
                    entity.addEffect(potionEffect.getEffect());
                }
            }
            // 应用食物效果
            if (needFood && entity instanceof Player player && (teaIngredient.canAlwaysDrink() || player.canEat(false)) && teaIngredient.getFoodValue().isValid()) {
                player.getFoodData().eat(teaIngredient.getFoodValue().nutrition, teaIngredient.getFoodValue().saturation);
                level.playSound(null, player.getOnPos(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1F, 1F);
            }
        }

        // 如果喝的是汤，就相当于吃掉汤物品
        List<SoupServingRecipe> soupServingRecipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
        soupServingRecipes.forEach(recipe -> {
            if (recipe.fluidToServe().getFluid() == fluidStack.getFluid() && fluidStack.getAmount() >= recipe.getAmountToServe()) {
                entity.eat(level, recipe.result().copy());
            }
        });

        // 应用原版药水tag的效果（这对机械动力的药水液体什么的会有用）
        List<MobEffectInstance> effects = PotionUtils.getAllEffects(fluidStack.getTag());
        for (var effect : effects) {
            if (!level.isClientSide) {
                if (effect.getDuration() == 0)
                    entity.addEffect(new MobEffectInstance(effect.getEffect(), 1, effect.getAmplifier()));
                else entity.addEffect(new MobEffectInstance(effect));
            }
        }
    }

    public static void showTeaIngredientTooltip(FluidStack fluidStack, List<Component> components) {
        if (fluidStack.isEmpty()) return;
        List<TeaIngredient> teaIngredients = TeaIngredient.readFromFluidStack(fluidStack);
        boolean clear = false;
        int fire = 0;
        int extinguishValue = 0;
        for (var ti : teaIngredients) {
            fire = fire + ti.getFireTime();
            if (ti.canClearAllEffect()) clear = true;
            extinguishValue = ti.canExtinguishing() ? extinguishValue + 1 : extinguishValue - 1;
        }
        if (extinguishValue == 0) fire = 0;
        if (clear) components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "tea_ingredient.clear", ChatFormatting.BLUE));
        if (extinguishValue > 0) components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "tea_ingredient.extinguishing", ChatFormatting.BLUE));
        else if (fire > 0) components.add(TextUtil.getPotionLikeTooltip(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "tea_ingredient.fire"), fire * 20));

        List<PotionEffect> potionEffects = new ArrayList<>(); // 所有茶属性中的药水效果，会有id重复的
        HashMap<String, PotionEffect> allEffects = new HashMap<>(); // 设定一个不重复id的effect表
        teaIngredients.forEach(ti -> potionEffects.addAll(ti.getEffects()));
        potionEffects.forEach(potionEffect -> {
            String id = potionEffect.id;
            PotionEffect effectPresent = allEffects.get(id);
            if (effectPresent != null) {
                if (effectPresent.amplifier < potionEffect.amplifier || (effectPresent.amplifier == potionEffect.amplifier && effectPresent.duration < potionEffect.duration)) {
                    allEffects.put(id, potionEffect);
                }
            } else {
                allEffects.put(id, potionEffect);
            } // 选择等级高的或者等级一样但是时间长的同种药水进行覆盖
        });
        List<MobEffectInstance> effects = new ArrayList<>();
        for (PotionEffect effect : allEffects.values()) {
            effects.add(effect.getEffect());
        }
        effects.addAll(PotionUtils.getAllEffects(fluidStack.getTag()));
        TextUtil.addPotionTooltipWithoutAttribute(effects, components);
    }

    /**
     * @return 设置每次喝掉的液体量（默认50），如果是汤，返回盛汤所需的液体量
     */
    public static int getDrinkVolume(Level level, FluidStack fluidStack) {
        List<SoupServingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
        for (var recipe : recipes) {
            if (recipe.fluidToServe().getFluid().isSame(fluidStack.getFluid())) return recipe.getAmountToServe();
        }
        return 50;
    }

    /**
     * @return 读取液体的茶成分，如果有饱食度那就根据玩家是否canEat决定能否吃，如果要能进食则必须所有ingredient都能持续可吃才行。
     */
    public static boolean canEat(FluidStack fluidStack, Player player) {
        List<TeaIngredient> teaIngredients = TeaIngredient.readFromFluidStack(fluidStack);
        boolean canAlwaysEat = teaIngredients.stream().allMatch(TeaIngredient::canAlwaysDrink);
        boolean isFoodLike = teaIngredients.stream().anyMatch(tea -> tea.getFoodValue().isValid());
        if (isFoodLike) return player.canEat(false) || canAlwaysEat;
        return true;
    }

}
