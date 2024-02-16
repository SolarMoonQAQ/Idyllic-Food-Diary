package cn.solarmoon.immersive_delight.common.fluid;

import cn.solarmoon.immersive_delight.common.fluid.base.AbstractHotFluid;
import cn.solarmoon.immersive_delight.common.registry.IMFluids;

/**
 * 热牛奶，站入其中受到伤害
 * 有4级以下的火焰保护不受伤害
 * 有防火药水或四级以上的火焰保护不受伤害且获得增益
 */
public class HotMilkFluid extends AbstractHotFluid {

    public HotMilkFluid() {
        super(IMFluids.HOT_MILK);
    }

}
