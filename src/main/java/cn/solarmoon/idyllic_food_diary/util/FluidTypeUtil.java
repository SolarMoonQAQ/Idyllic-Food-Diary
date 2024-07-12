package cn.solarmoon.idyllic_food_diary.util;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;

import static cn.solarmoon.idyllic_food_diary.registry.common.IMFluids.*;

@Deprecated
public class FluidTypeUtil {


    /**
     * 用于判断输入值是否与任一模组的液体匹配
     */
    public static boolean IMFluidsMatch(FluidType fluidType) {
        for(var type : FluidTypes) {
            if (type.equals(fluidType)) return true;
        }
        return false;
    }

    /**
     * 用于判断实体是否在任一热水中
     */
    public static boolean isInHotFluid(Entity entity) {
        for(var type : HotFluidTypes) {
            if (entity.isInFluidType(type)) return true;
        }
        return false;
    }

    /**
     * 用于判断实体是否在任一可生成粒子效果气泡的水中
     */
    public static boolean isInFluid(Entity entity) {
        for(var type : FluidTypes) {
            if (entity.isInFluidType(type)) return true;
        }
        return false;
    }

    public static List<FluidType> HotFluidTypes = new ArrayList<>(List.of(
    ));

    public static List<FluidType> FluidTypes = new ArrayList<>(List.of(
            BLACK_TEA.getTypeObject().get(),
            GREEN_TEA.getTypeObject().get(),
            MILK_TEA.getTypeObject().get()
    ));

}
