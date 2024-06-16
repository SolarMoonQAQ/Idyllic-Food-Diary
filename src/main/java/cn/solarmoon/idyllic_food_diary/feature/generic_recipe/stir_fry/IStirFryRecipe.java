package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPendingResult;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.blockentity_util.ITankBE;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IStirFryRecipe extends IContainerBE, ITankBE, IHeatable, ISpiceable, IPendingResult {

    String STIR_FRY_TIME = "StirFryTime";
    String STIR_FRY_RECIPE_TIME = "StirFryRecipeTime";
    String STIR_FRY_COUNT = "StirFryCount";
    String STIR_FRY_RECIPE = "StirFryRecipe";
    String STIR_FRY_STAGE_NUM = "StirFryStageNum";
    String STIR_FRY_CAN = "StirFryCan";
    String STIR_FRY_PENDING = "StirFryPending";

    default boolean tryStirFrying() {
        Level level = h().getLevel();
        AnimTicker animTicker = h().getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).orElse(null).getAnimTicker(2);
        if (level == null) return false;

        // 如果当前物品满足任意配方的阶段0，则保存该配方
        findStirFryRecipe().ifPresent(recipe -> {
            if (getStirFryRecipe() == null) {
                Optional<?> rop = level.getRecipeManager().byKey(recipe.id());
                rop.ifPresent(r -> setStirFryRecipe((StirFryRecipe) r));
            }
        });

        // 如果当前物品没有满足当前配方阶段-1的所有物品，则删除保存的配方
        if (getStirFryRecipe() != null) {
            List<Ingredient> ingredients = new ArrayList<>();
            int stage = getPresentStage() == 0 ? 0 : getPresentStage() - 1; // 同时，第一阶段是必须随时满足的
            for (int i = 0; i <= stage; i++) {
                ingredients.addAll(getStirFryRecipe().stirFryStages().get(i).ingredients());
            } // 下面同时检测了物品和需要材料长度是否一致，否则可能出现同一个材料匹配多个物品的情况
            if (!ingredients.stream().allMatch(in -> getStacks().stream().anyMatch(in) && getStacks().size() >= ingredients.size())) {
                setStirFryRecipe(null);
            }
        }

        // 有配方（也就是在首次条件中完全满足了阶段0），进入烹饪
        if (getStirFryRecipe() != null) {
            if (getPresentFryStage() != null) { // 这里当前阶段不为null，意味着还在配方所需阶段范围内
                setStirFryRecipeTime(getPresentFryStage().time());
                // 对每个阶段进行内容物检查，要求：满足所有阶段物品所需之和
                List<Ingredient> ingredients = new ArrayList<>();
                for (int i = 0; i <= getPresentStage(); i++) {
                    ingredients.addAll(getStirFryRecipe().stirFryStages().get(i).ingredients());
                }
                // 当物品和液体全匹配后，进行该阶段的正式烹饪
                if (ingredients.stream().allMatch(in -> getStacks().stream().anyMatch(in) && getStacks().size() == ingredients.size()) && isFluidMatch()) {
                    if (getStirFryTime() >= getPresentFryStage().time()) {
                        setCanStirFry(true); // 设置可以翻炒
                        if (getFryCount() >= getPresentFryStage().fryCount() && !animTicker.isEnabled()) { // 最终步骤完成
                            setCanStirFry(false);
                            setPendingItem(getPendingItem().isEmpty() ? getStirFryRecipe().result().copy() : getPendingItem());
                            addSpicesToItem(getPendingItem(), false);
                            if (!getPresentFryStage().keepFluid()) clearTank();
                            setStirFryTime(0);
                            setStirFryRecipeTime(0);
                            setFryCount(0); //重置各个计数
                            setPresentStage(getPresentStage() + 1); //到下个阶段
                            h().setChanged();
                        }
                    } else if (withTrueSpices(getPresentFryStage().spices(), true)) {
                        setStirFryTime(getStirFryTime() + 1);
                        IdyllicFoodDiary.DEBUG.send(getStirFryTime() + "/" + getStirFryRecipeTime());
                    }
                }
            } else { // 此处就代表当前阶段已经超出配方最大阶段，表示所有阶段已满足
                setPending(getPendingItem(), getStirFryRecipe().container());
                addSpicesToItem(getResult(), true);
                clearInv();
            }
            return true;
        } else {
            setPresentStage(0);
            setStirFryTime(0);
            setStirFryRecipeTime(0);
            setFryCount(0); //重置各个计数
            return false;
        }

    }

    default boolean isStirFrying() {
        return getStirFryRecipe() != null;
    }

    default boolean doStirFry() {
        AnimTicker animTicker = h().getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).orElse(null).getAnimTicker(2);
        if (canStirFry() && getPresentFryStage() != null && !animTicker.isEnabled()) {
            setFryCount(getFryCount() + 1);
            animTicker.start();
            return true;
        }
        return false;
    }

    /**
     * @return 当前液体是否匹配当前阶段所需的液体
     */
    default boolean isFluidMatch() {
        if (getPresentFryStage() != null) {
            return FluidUtil.isMatch(getTank().getFluid(), getPresentFryStage().fluidStack(), true, false);
        }
        return false;
    }

    /**
     * @return 获取当前的翻炒阶段
     */
    @Nullable
    default StirFryStage getPresentFryStage() {
        if (getStirFryRecipe() != null && getPresentStage() < getStirFryRecipe().stirFryStages().size()) {
            return getStirFryRecipe().stirFryStages().get(getPresentStage());
        }
        return null;
    }

    /**
     * @return 这里与其它的配方略有不同，这里是对配方进行首次寻找，找到以后就没用了
     */
    default Optional<StirFryRecipe> findStirFryRecipe() {
        Level level = h().getLevel();
        if (level == null) return Optional.empty();
        List<StirFryRecipe> recipes = level.getRecipeManager().getAllRecipesFor(IMRecipes.STIR_FRY.get());
        return recipes.stream().filter(recipe -> {
            boolean inMatch = getStacks().stream().allMatch(stack ->
                    recipe.getFirstStage().ingredients().stream().anyMatch(ingredient -> ingredient.test(stack)));
            boolean fluidMatch = FluidUtil.isMatch(getTank().getFluid(), recipe.getFirstStage().fluidStack(), true, false);
            boolean heat = isOnHeatSource();
            return inMatch && fluidMatch && heat;
        }).findFirst();
    }

    @Nullable
    StirFryRecipe getStirFryRecipe();

    void setStirFryRecipe(StirFryRecipe recipe);

    int getPresentStage();

    void setPresentStage(int stage);

    int getStirFryTime();

    void setStirFryTime(int time);

    int getStirFryRecipeTime();

    void setStirFryRecipeTime(int time);

    boolean canStirFry();

    void setCanStirFry(boolean or);

    int getFryCount();

    void setFryCount(int count);

    ItemStack getPendingItem();

    void setPendingItem(ItemStack stack);

}
