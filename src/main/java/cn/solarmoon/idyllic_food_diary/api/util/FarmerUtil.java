package cn.solarmoon.idyllic_food_diary.api.util;

import cn.solarmoon.idyllic_food_diary.api.data.serializer.TeaIngredient;
import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.common.recipe.SoupServingRecipe;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMRecipes;
import cn.solarmoon.idyllic_food_diary.core.compat.create.util.PotionUtil;
import cn.solarmoon.idyllic_food_diary.core.compat.farmersdelight.util.FarmersUtil;
import cn.solarmoon.idyllic_food_diary.api.data.serializer.FluidEffect;
import cn.solarmoon.idyllic_food_diary.core.data.tags.IMBlockTags;
import cn.solarmoon.solarmoon_core.api.data.serializable.FoodValue;
import cn.solarmoon.solarmoon_core.api.data.serializable.PotionEffect;
import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class FarmerUtil {

    /**
     * 检查是否为热源
     */
    public static boolean isHeatSource(BlockState state) {
        boolean commonFlag = state.is(IMBlockTags.HEAT_SOURCE) || FarmersUtil.isHeatSource(state);
        if (state.hasProperty(LIT)) {
            return commonFlag && state.getValue(LIT);
        }
        return commonFlag;
    }

    /**
     * 吐子儿
     */
    public static void spit(Item item, int count, Player player) {
        Level level = player.level();
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(player, 0.5, -0.5);
        ItemEntity itemEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, new ItemStack(item, count));
        itemEntity.addDeltaMovement(player.getLookAngle().scale(0.3));
        itemEntity.setPickUpDelay(20);
        level.addFreshEntity(itemEntity);
    }

    /**
     * 吐子儿
     */
    public static void spit(ItemStack stack, Player player) {
        Level level = player.level();
        Vec3 spawnPos = VecUtil.getSpawnPosFrontEntity(player, 0.5, -0.5);
        ItemEntity itemEntity = new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, stack);
        itemEntity.addDeltaMovement(player.getLookAngle().scale(0.3));
        itemEntity.setPickUpDelay(20);
        level.addFreshEntity(itemEntity);
    }

    /**
     * 为了和block的use方法互通
     * 相当于应用所有液体效果
     */
    public static void commonDrink(FluidStack fluidStack, Level level, LivingEntity entity, boolean needFood) {
        //根据液体id获取对应的FluidEffect数据
        String fluidId = fluidStack.getFluid().getFluidType().toString();
        String fluidTag = fluidStack.getTag() != null ? fluidStack.getTag().toString() : "empty";
        IdyllicFoodDiary.DEBUG.send("喝下液体：" + fluidId + " " + fluidTag);

        //机械动力联动：根据药水tag获取药水效果
        PotionUtil.applyPotionFluidEffect(fluidTag, entity, level);

        FluidEffect fluidEffect = FluidEffect.get(fluidId);
        if(fluidEffect != null) {
            //获取potion（因为多种药水效果并行所以为s）
            List<PotionEffect> potionEffects = fluidEffect.effects;
            //如果clear为true则先清空药水效果
            if(fluidEffect.clear) if(!level.isClientSide) entity.removeAllEffects();
            //如果fire不为0就点燃
            if(fluidEffect.fire > 0) if(!level.isClientSide) entity.setSecondsOnFire(fluidEffect.fire);
            //如果extinguishing为true就灭火
            if(fluidEffect.extinguishing) if(!level.isClientSide) entity.clearFire();
            //如果foodValue有作用就加饱食度
            if(fluidEffect.getFoodValue().isValid() && needFood) {
                if(entity instanceof Player player) {
                    if(player.canEat(false) || fluidEffect.canAlwaysDrink) {
                        FoodValue foodValue = fluidEffect.getFoodValue();
                        if(!level.isClientSide) {
                            player.getFoodData().eat(foodValue.nutrition, foodValue.saturation);
                            level.playSound(null, player.getOnPos(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1F, 1F);
                        }
                    }
                }
            }
            //把每种药水效果按概率应用于玩家
            if(potionEffects != null) {
                for (var potion : potionEffects) {
                    MobEffectInstance mobEffectInstance = potion.getEffect();
                    double chance = potion.getChance();
                    Random random = new Random();
                    double rand = random.nextDouble();
                    if (rand <= chance) {
                        if(!level.isClientSide) entity.addEffect(mobEffectInstance);
                    }
                    if (entity instanceof Player player) {
                        IdyllicFoodDiary.DEBUG.send("存在药水效果：" + mobEffectInstance, player);
                    }
                }
            }
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
        // 应用灭火效果  <- 先着火再灭火，这样如果有着火和灭火的搭配起来就可以不着火
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

        PotionUtil.applyPotionFluidEffect(fluidStack.getOrCreateTag().toString(), entity, level);
    }

    /**
     * @return 设置每次喝掉的液体量（默认50）
     */
    public static int getDrinkVolume(Level level, FluidStack fluidStack) {
        List<SoupServingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.SOUP_SERVING.get());
        for (var recipe : recipes) {
            if (recipe.fluidToServe().getFluid().isSame(fluidStack.getFluid())) return recipe.getAmountToServe();
        }
        return 50;
    }

    /**
     * 显示药水效果数据提示
     */
    public static void showFluidEffectTooltip(FluidEffect fluidEffect, List<Component> components) {
        if (fluidEffect != null) {
            List<PotionEffect> potionEffects = fluidEffect.effects;
            if (potionEffects != null) {
                for (var effect : potionEffects) {
                    Component base = TextUtil.getMinuteFormatEffectDuration(effect.getEffect());
                    components.add(base);
                }
            }
            if (fluidEffect.clear)
                components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_effect_clear", ChatFormatting.BLUE));
            if (fluidEffect.extinguishing)
                components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_effect_extinguishing", ChatFormatting.BLUE));
            if (fluidEffect.fire > 0)
                components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "fluid_effect_fire", ChatFormatting.BLUE, fluidEffect.fire));
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
        else if (fire > 0) components.add(IdyllicFoodDiary.TRANSLATOR.set("tooltip", "tea_ingredient.fire", ChatFormatting.BLUE, TextUtil.toMinuteFormat(fire, true)));

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
        for (PotionEffect effect : allEffects.values()) {
            Component effectC = TextUtil.getMinuteFormatEffectDuration(effect.getEffect());
            components.add(effectC);
        }
    }

}
