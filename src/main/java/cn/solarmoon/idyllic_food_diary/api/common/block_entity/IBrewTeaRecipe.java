package cn.solarmoon.idyllic_food_diary.api.common.block_entity;

import cn.solarmoon.idyllic_food_diary.api.data.serializer.FluidBoundMap;
import cn.solarmoon.idyllic_food_diary.api.data.serializer.TeaIngredient;
import cn.solarmoon.idyllic_food_diary.api.util.FarmerUtil;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import com.google.gson.Gson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 直接接入就能实现泡茶配方
 */
public interface IBrewTeaRecipe extends IContainerBlockEntity, ITankBlockEntity {

    default BlockEntity self2() {
        return (BlockEntity) this;
    }

    /**
     * 放于tick中将会尝试烹饪
     */
    default void tryBrewTea() {
        if (canStartBrew()) {
            setBrewRecipeTime(getNeedTime());
            setBrewTime(getBrewTime() + 1);
            if (getBrewTime() >= getBrewRecipeTime()) {
                findBase().ifPresent(base -> {
                    TeaIngredient baseAttribute = getBaseTeaAttribute();
                    Fluid outputFluid = getOutputFluidType();
                    if (baseAttribute != null && outputFluid != null) {
                        int amount = getTank().getFluidAmount() - getFluidConsumption();
                        CompoundTag tag = new CompoundTag();
                        Gson gson = new Gson();
                        String json = gson.toJson(getTeaIngredients());
                        tag.putString("TeaIngredients", json);
                        putNameToNBT(tag);
                        FluidStack result = new FluidStack(outputFluid, amount, tag);
                        getTank().setFluid(result);
                        getStacksHasTeaAttribute().forEach(stack -> stack.shrink(1));
                        self2().setChanged();
                    }
                });
            }
        } else setBrewTime(0);
    }

    default boolean isBrewing() {
        return getBrewTime() > 0;
    }

    default void putNameToNBT(CompoundTag tag) {
        CompoundTag tag0 = new CompoundTag();
        for (ItemStack stack : getStacks()) {
            TeaIngredient teaIngredient = getTeaIngredient(stack);
            if (teaIngredient != null) {
                tag0.putString(teaIngredient.getType().toString(), stack.getDescriptionId());
            }
        }
        tag.put("CustomName", tag0);
    }

    /**
     * @return 下方是否有热源
     */
    default boolean isOnHeatSource() {
        Level level = self2().getLevel();
        if (level != null) {
            BlockState state = level.getBlockState(self2().getBlockPos().below());
            return FarmerUtil.isHeatSource(state);
        }
        return false;
    }

    /**
     * @return 找到三个材料中的基底材料（有重复就只找第一个找到的）
     */
    default Optional<ItemStack> findBase() {
        List<ItemStack> materials = getStacks();
        return materials.stream()
                .filter(stack -> {
                    for (TeaIngredient teaIngredient : TeaIngredient.getAll()) {
                        if (teaIngredient.getIngredient().test(stack) && teaIngredient.isBase()) {
                            return true;
                        }
                    }
                    return false;
                })
                .findFirst();
    }

    /**
     * 逻辑：找到第一个是base的材料，返回它的tea属性
     * @return 快速获取容器内的base茶属性，但可能为null，一般确定了不为null可以简化一些代码过程
     */
    @Nullable
    default TeaIngredient getBaseTeaAttribute() {
        if (findBase().isPresent()) {
            ItemStack base = findBase().get();
            return getTeaIngredient(base);
        }
        return null;
    }

    /**
     * @return 获取所有存在tea属性的物品
     */
    default List<ItemStack> getStacksHasTeaAttribute() {
        List<ItemStack> stacks = new ArrayList<>();
        for (var stack : getStacks()) {
            TeaIngredient.getAll().forEach(teaIngredient -> {
                if (teaIngredient.getIngredient().test(stack)) {
                    stacks.add(stack);
                }
            });
        }
        return stacks;
    }

    /**
     * @return 是否满足开始烹饪条件
     */
    default boolean canStartBrew() {
        TeaIngredient teaIngredient = getBaseTeaAttribute();
        if (teaIngredient != null) {
            // 壶内液体和基底所对应的内液体一致
            boolean flag1 = isBaseFluidTypeMatch();
            // 壶内液体量小于三个材料冲泡量相加 并且 壶内液体量大于最终消耗量
            boolean flag2 = getTank().getFluidAmount() <= getMaxBrewingVolume() && getTank().getFluidAmount() >= getFluidConsumption();
            // 判断是否需要加热
            boolean flag3 = !needHeating() || isOnHeatSource();
            // 所有物品都有tea属性
            boolean flag4 = allHasTeaAttribute();
            // base物品只能有一个
            boolean flag5 = isSingleBaseIngredient();
            return flag1 && flag2 && flag3 && flag4 && flag5;
        }
        return false;
    }

    /**
     * @return 是否只有一个base类型的物品
     */
    default boolean isSingleBaseIngredient() {
        int baseAmount = 0;
        for (var ti : getTeaIngredients()) {
            baseAmount = ti.isBase() ? baseAmount + 1 : baseAmount;
        }
        return baseAmount == 1;
    }

    /**
     * @return 比较容器内液体类型是否和第一个找到的base材料的类型一致
     */
    default boolean isBaseFluidTypeMatch() {
        TeaIngredient teaIngredient = getBaseTeaAttribute();
        if (teaIngredient != null) {
            for (FluidBoundMap map : teaIngredient.getFluidBoundMap()) {
                if (getTank().getFluid().getFluid() == map.getInput()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return 自动根据base的茶属性和对应的输入液体查找匹配的输出液体类型
     */
    @Nullable
    default Fluid getOutputFluidType() {
        if (findBase().isPresent()) {
            ItemStack base = findBase().get();
            TeaIngredient teaIngredient = getTeaIngredient(base);
            if (teaIngredient != null) {
                Optional<FluidBoundMap> fluidOp = teaIngredient.getFluidBoundMap().stream()
                        .filter(map -> map.getInput() == getTank().getFluid().getFluid())
                        .findFirst();
                if (fluidOp.isPresent()) {
                    return fluidOp.get().getOutput();
                }
            }
        }
        return null;
    }

    /**
     * 获取物品的对应茶属性<br/>
     * 逻辑：如果直接比较ingredient，会导致在tag和item同时匹配的情况下无法区分出想要的内容，因而在类型为base时还需检测液体绑定的匹配以防卡在第一个匹配的属性上导致不工作
     */
    @Nullable
    default TeaIngredient getTeaIngredient(ItemStack stack) {
        for (TeaIngredient teaIngredient : TeaIngredient.getAll()) {
            boolean boundMatch = teaIngredient.getFluidBoundMap().isEmpty();
            for (FluidBoundMap map : teaIngredient.getFluidBoundMap()) {
                if (map.getInput() == getTank().getFluid().getFluid()) {
                    boundMatch = true;
                }
            }
            if (teaIngredient.getIngredient().test(stack) && boundMatch) {
                return teaIngredient;
            }
        }
        return null;
    }

    /**
     * @return 获取所有材料中存在的属性，但只在输入液体匹配base的绑定输入液体时才能正常获取
     */
    default List<TeaIngredient> getTeaIngredients() {
        List<TeaIngredient> teaIngredients = new ArrayList<>();
        List<ItemStack> stacks = getStacks();
        stacks.forEach(stack -> {
            TeaIngredient teaIngredient = getTeaIngredient(stack);
            if (teaIngredient != null) {
                teaIngredients.add(teaIngredient);
            }
        });
        return teaIngredients;
    }

    /**
     * @return 是否其中所有物品都含有茶属性
     */
    default boolean allHasTeaAttribute() {
        for (var stack : getStacks()) {
            if (getTeaIngredient(stack) == null) {
                return false;
            }
        }
        return !getTeaIngredients().isEmpty();
    }

    /**
     * @return 材料最大可冲泡液体量之和
     */
    default int getMaxBrewingVolume() {
        int i = 0;
        for (TeaIngredient teaIngredient : getTeaIngredients()) {
            i = i + teaIngredient.getMaxBrewingVolume();
        }
        return i;
    }

    /**
     * @return 材料消耗液体总量之和
     */
    default int getFluidConsumption() {
        int i = 0;
        for (TeaIngredient teaIngredient : getTeaIngredients()) {
            i = i + teaIngredient.getFluidConsumption();
        }
        return i;
    }

    /**
     * @return 材料所需时间之和
     */
    default int getNeedTime() {
        int i = 0;
        for (TeaIngredient teaIngredient : getTeaIngredients()) {
            i = i + teaIngredient.getTime();
        }
        return i;
    }

    /**
     * @return 如果所有材料中有一个需要加热，那就需要加热
     */
    default boolean needHeating() {
        for (TeaIngredient teaIngredient : getTeaIngredients()) {
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
