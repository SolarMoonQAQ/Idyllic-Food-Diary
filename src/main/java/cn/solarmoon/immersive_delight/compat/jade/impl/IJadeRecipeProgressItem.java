package cn.solarmoon.immersive_delight.compat.jade.impl;

/**
 * jade联动：给有绑定配方的物品提供其配方完成所需时间的接口<br/>
 * 方块不需要接口，因为方块可以直接绑定方块，非常方便
 */
public interface IJadeRecipeProgressItem {

    int getRecipeTime();

}
