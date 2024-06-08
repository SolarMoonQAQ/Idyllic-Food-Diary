package cn.solarmoon.idyllic_food_diary.feature.logic.basic_feature;

import cn.solarmoon.solarmoon_core.api.common.block.ILitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public interface ISimpleFuelBlockEntity extends ILitBlock {

    default BlockEntity sf() {
        return (BlockEntity) this;
    }

    /**
     * @return 返回要消耗和计算的燃料
     */
    ItemStack getFuel();

    default void tryControlLit() {
        BlockState state = sf().getBlockState();
        BlockPos pos = sf().getBlockPos();
        Level level = sf().getLevel();
        //消耗煤炭，控制lit属性
        if (state.getValue(LIT)) {
            //有燃料就保存其燃烧时间，并且消耗一个
            if (!noFuel() && getFuelTime() == 0) {
                setFuelTime(ForgeHooks.getBurnTime(getFuel(), null));
                getFuel().shrink(1);
                sf().setChanged();
            }
            setBurnTime(getBurnTime() + 1);
            //燃烧时间超过燃料所能提供，就刷新
            if (getBurnTime() >= getFuelTime()) {
                setBurnTime(0);
                setFuelTime(0);
            }
            //无燃料且没有燃烧时间了就停止lit
            if (noFuel() && getBurnTime() == 0) {
                if (level != null) {
                    level.setBlock(pos, state.setValue(LIT, false), 3);
                }
                sf().setChanged();
            }
        }
    }

    default boolean noFuel() {
        return getFuel().isEmpty();
    }

    int getBurnTime();

    void setBurnTime(int time);

    int getFuelTime();

    void setFuelTime(int time);

}
