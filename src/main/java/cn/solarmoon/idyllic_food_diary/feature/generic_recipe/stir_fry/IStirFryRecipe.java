package cn.solarmoon.idyllic_food_diary.feature.generic_recipe.stir_fry;

import cn.solarmoon.idyllic_food_diary.api.AnimHelper;
import cn.solarmoon.idyllic_food_diary.api.AnimTicker;
import cn.solarmoon.idyllic_food_diary.element.matter.inlaid_stove.IBuiltInStove;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IPlateable;
import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.network.NETList;
import cn.solarmoon.idyllic_food_diary.registry.common.IMPacks;
import cn.solarmoon.idyllic_food_diary.registry.common.IMRecipes;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.solarmoon_core.api.tile.fluid.FluidHandlerUtil;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import cn.solarmoon.solarmoon_core.feature.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import kotlin.Unit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public interface IStirFryRecipe extends IContainerTile, ITankTile, IHeatable, ISpiceable, IPlateable {

    String STIR_FRY_TIME = "StirFryTime";
    String STIR_FRY_RECIPE_TIME = "StirFryRecipeTime";
    String STIR_FRY_COUNT = "StirFryCount";
    String STIR_FRY_RECIPE = "StirFryRecipe";
    String STIR_FRY_STAGE_NUM = "StirFryStageNum";
    String STIR_FRY_CAN = "StirFryCan";
    String STIR_FRY_PENDING = "StirFryPending";

    default boolean tryStirFrying() {
        Level level = h().getLevel();
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
            final SpiceList match = new SpiceList();
            getStirFryRecipe().stirFryStages().forEach(stage -> match.addAll(stage.spices()));
            if (getPresentFryStage() != null) { // 这里当前阶段不为null，意味着还在配方所需阶段范围内
                setStirFryRecipeTime(getPresentFryStage().time());
                // 对每个阶段进行内容物检查，要求：满足所有阶段物品所需之和
                List<Ingredient> ingredients = new ArrayList<>();
                for (int i = 0; i <= getPresentStage(); i++) {
                    ingredients.addAll(getStirFryRecipe().stirFryStages().get(i).ingredients());
                }
                // 当物品和液体全匹配后，进行该阶段的正式烹饪
                if (ingredients.stream().allMatch(in -> getStacks().stream().anyMatch(in) && getStacks().size() == ingredients.size()) && isFluidMatch()) {
                    if (getStirFryTime() > getPresentFryStage().time()) {
                        setCanStirFry(true); // 设置可以翻炒
                        if (isAnimFin() && getFryCount() >= getPresentFryStage().fryCount()) { // 最终步骤完成
                            setPendingItem(getPendingItem().isEmpty() ? getStirFryRecipe().result().copy() : getPendingItem());
                            addSpicesToItem(null, getPendingItem(), false);
                            if (!getPresentFryStage().keepFluid()) FluidHandlerUtil.clearTank(getTank());
                            setStirFryTime(0);
                            setStirFryRecipeTime(0);
                            setFryCount(0); //重置各个计数
                            setPresentStage(getPresentStage() + 1); //到下个阶段
                            setCanStirFry(false);
                            h().setChanged();
                        }
                    } else if (withTrueSpices(getPresentFryStage().spices(), true)) {
                        setStirFryTime(getStirFryTime() + 1);
                    }
                }
            } else { // 此处就代表当前阶段已经超出配方最大阶段，表示所有阶段已满足
                setPending(getPendingItem(), getStirFryRecipe().container());
                addSpicesToItem(match, getResult(), true);
                ItemHandlerUtil.clearInv(getInventory(), h());
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

    /**
     * @return 配方是否在运作
     */
    default boolean isStirFrying() {
        return getStirFryRecipe() != null && getPresentFryStage() != null;
    }

    default boolean doStirFry() {
        if (!isAnimFin() || getPresentFryStage() == null || getFryCount() >= getPresentFryStage().fryCount()) return false;

        Level level = h().getLevel();
        if (level == null) return false;

        boolean flag = false;

        for (int i = 0; i < getStacks().size(); i++) {
            String name = "fry" + i;
            AnimHelper.createMap(h(), name);
            AnimTicker anim = AnimHelper.getMap(h()).get(name);
            anim.getTimer().setMaxTime(getFryTime(getStacks().size()));
            if (canStirFry() && !anim.getTimer().getTiming()) {
                anim.getFixedValues().put("rotRandom", new Random().nextFloat()); // 设置炒菜随机旋转角
                anim.getFixedValues().put("maxHeight", getFryHeight(i));
                int finalI = i;
                anim.getTimer().setOnStopAction(() -> {
                    double posYOffset = 0;
                    if (h().getBlockState().getBlock() instanceof IBuiltInStove b) {
                        posYOffset = b.getYOffset(h().getBlockState());
                    }
                    // 每一片食材落下后产生火花
                    double xInRange = 2 / 16f + new Random().nextDouble() * 12 / 16;
                    double zInRange = 2 / 16f + new Random().nextDouble() * 12 / 16; // 保证粒子起始点在锅内
                    double vi = (new Random().nextDouble() - 0.5) / 5;
                    level.addParticle(ParticleTypes.SMALL_FLAME, h().getBlockPos().getX() + xInRange, h().getBlockPos().getY() + (1.5 + finalI * 0.25)/16f + posYOffset, h().getBlockPos().getZ() + zInRange, vi, 0.1, vi);
                    return Unit.INSTANCE;
                });
                anim.getTimer().start();
                flag = true;
            }
        }

        if (flag) {
            setFryCount(getFryCount() + 1);
            level.playSound(null, h().getBlockPos(), IMSounds.STIR_SIZZLE.get(), SoundSource.BLOCKS);
            return true;
        }

        return false;
    }

    /**
     * @return 每道菜翻炒能到达的最大高度
     */
    default float getFryHeight(int index) {
        return (float) (12/16f + 0.25 * index);
    }

    /**
     * @return 自由落体时间
     */
    default int getFryTime(int index) {
        double g = 9.8;
        double t = Math.sqrt(getFryHeight(index) / g);
        return (int) (t * 30);
    }

    default boolean isAnimFin() {
        AnimTicker anim = AnimHelper.getMap(h()).get("fry" + (getStacks().size()-1));
        return anim == null || !anim.getTimer().getTiming();
    }

    /**
     * @return 当前液体是否匹配当前阶段所需的液体
     */
    default boolean isFluidMatch() {
        if (getPresentFryStage() != null) {
            return FluidHandlerUtil.isMatch(getTank().getFluid(), getPresentFryStage().fluidStack(), true, false);
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

    @Nullable
    default StirFryStage getFryStage(int stage) {
        if (getStirFryRecipe() != null) {
            return getStirFryRecipe().stirFryStages().get(stage);
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
            boolean fluidMatch = FluidHandlerUtil.isMatch(getTank().getFluid(), recipe.getFirstStage().fluidStack(), true, false);
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
