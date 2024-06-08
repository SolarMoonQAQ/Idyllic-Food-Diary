package cn.solarmoon.idyllic_food_diary.element.matter.fluid;

import cn.solarmoon.idyllic_food_diary.registry.common.IMFluids;

/**
 * 热水，站入其中受到伤害
 * 有4级以下的火焰保护不受伤害
 * 有防火药水或四级以上的火焰保护不受伤害且获得增益
 */
public class HotWaterFluid extends AbstractHotFluid {

    public HotWaterFluid() {
        super(IMFluids.HOT_WATER);
    }

}
