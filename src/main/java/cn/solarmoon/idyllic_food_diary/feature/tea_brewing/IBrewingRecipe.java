package cn.solarmoon.idyllic_food_diary.feature.tea_brewing;

import cn.solarmoon.idyllic_food_diary.feature.basic_feature.IHeatable;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.ITempChanger;
import cn.solarmoon.idyllic_food_diary.feature.fluid_temp.Temp;
import cn.solarmoon.solarmoon_core.api.tile.inventory.ItemHandlerUtil;
import com.google.gson.Gson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cn.solarmoon.idyllic_food_diary.feature.tea_brewing.TeaBrewingUtil.*;

/**
 * 直接接入就能实现泡茶配方
 */
public interface IBrewingRecipe extends IHeatable, ITempChanger {

    String BREW_TICK = "BrewTick";

    /**
     * 放于tick中将会尝试烹饪
     */
    default boolean tryBrewTea() {
        FluidStack fluidStack = getTank().getFluid();
        tryApplyThermochanger();
        if (canStartBrew()) {
            setBrewRecipeTime(getNeedTime());
            setBrewTime(getBrewTime() + 1);
            h().setChanged();
            if (getBrewTime() >= getBrewRecipeTime()) {
                if (getValidFluidBound() != null) {
                    int amount = getTank().getFluidAmount() - getFluidConsumption();
                    CompoundTag tag = new CompoundTag();
                    Gson gson = new Gson();
                    String json = gson.toJson(getAllAttributes());
                    tag.putString(TeaIngredient.TAG_KEY, json);
                    putNameToNBT(tag);
                    FluidStack result = new FluidStack(getValidFluidBound().getOutput(), amount, tag);
                    Temp.set(result, Temp.get(fluidStack));
                    getTank().setFluid(result);
                    getStacks().forEach(stack -> stack.shrink(1));
                    h().setChanged();
                }
            }
            return true;
        } else {
            setBrewTime(0);
            return false;
        }
    }

    default boolean isBrewing() {
        return getBrewTime() > 0;
    }

    default void putNameToNBT(CompoundTag tag) {
        CompoundTag tag0 = new CompoundTag();
        for (ItemStack stack : getStacks()) {
            TeaIngredient teaIngredient = getTeaIngredient(stack);
            if (teaIngredient != null) {
                String name = teaIngredient.getDisplay().isEmpty() ? stack.getDescriptionId() : teaIngredient.getDisplay();
                tag0.putString(teaIngredient.getType().toString(), name);
            }
        }
        tag.put(TeaIngredient.CUSTOM_NAME, tag0);
    }

    /**
     * @return 找第一个为base属性的茶成分
     */
    @Nullable
    default TeaIngredient.Base getBaseAttribute() {
        for (ItemStack stack : getStacks()) {
            var at = getTeaIngredient(stack);
            if (at != null && at.getType() == TeaIngredient.Type.BASE) {
                return (TeaIngredient.Base) at;
            }
        }
        return null;
    }

    /**
     * @return 获取当前匹配的配方
     */
    @Nullable
    default FluidBoundMap getValidFluidBound() {
        if (getBaseAttribute() != null) {
            for (var map : getBaseAttribute().getFluidBoundMap()) {
                if (
                        map.getInput() == getTank().getFluid().getFluid()
                        && Temp.isSame(getTank().getFluid(), map.getTemp())
                ) {
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * @return 获取所有物品的对应属性集合
     */
    default List<TeaIngredient> getAllAttributes() {
        List<TeaIngredient> list = new ArrayList<>();
        for (ItemStack stack : getStacks()) {
            if (getTeaIngredient(stack) != null) {
                list.add(getTeaIngredient(stack));
            }
        }
        return list;
    }

    default List<ItemStack> getStacks() {
        return ItemHandlerUtil.getStacks(getInventory());
    }

    /**
     * @return 是否满足开始烹饪条件
     */
    default boolean canStartBrew() {
        // 壶内液体匹配基底配方所需绑定组
        boolean flag1 = getValidFluidBound() != null;
        // 壶内液体量小于三个材料冲泡量相加 并且 壶内液体量大于最终消耗量
        boolean flag2 = getTank().getFluidAmount() <= getMaxBrewingVolume() && getTank().getFluidAmount() >= getFluidConsumption();
        // 判断是否需要加热
        boolean flag3 = !needHeating() || isOnHeatSource();
        // 所有物品都有tea属性
        boolean flag4 = getAllAttributes().size() == getStacks().size();
        return flag1 && flag2 && flag3 && flag4;
    }

    /**
     * @return 材料最大可冲泡液体量之和
     */
    default int getMaxBrewingVolume() {
        int i = 0;
        for (TeaIngredient teaIngredient : getAllAttributes()) {
            i = i + teaIngredient.getMaxBrewingVolume();
        }
        return i;
    }

    /**
     * @return 材料消耗液体总量之和
     */
    default int getFluidConsumption() {
        int i = 0;
        for (TeaIngredient teaIngredient : getAllAttributes()) {
            i = i + teaIngredient.getFluidConsumption();
        }
        return i;
    }

    /**
     * 这里是直接计算的时间，和下面的getBrewRecipeTime不冲突
     * @return 材料所需时间之和
     */
    default int getNeedTime() {
        int i = 0;
        for (TeaIngredient teaIngredient : getAllAttributes()) {
            i = i + teaIngredient.getTime();
        }
        return i;
    }

    /**
     * @return 如果所有材料中有一个需要加热，那就需要加热
     */
    default boolean needHeating() {
        for (TeaIngredient teaIngredient : getAllAttributes()) {
            if (teaIngredient.needHeating()) {
                return true;
            }
        }
        return false;
    }

    void setBrewTime(int time);
    
    void setBrewRecipeTime(int time);
    
    int getBrewTime();
    
    int getBrewRecipeTime();

}
