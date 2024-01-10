package cn.solarmoon.immersive_delight.common.crops;

import cn.solarmoon.immersive_delight.common.IMCrops;
import cn.solarmoon.immersive_delight.common.crops.abstract_crops.TeaCrop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GreenTeaCrop extends TeaCrop {

    public GreenTeaCrop() {

    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        return new ItemStack(IMCrops.GREEN_TEA_LEAVES.get());
    }

}
