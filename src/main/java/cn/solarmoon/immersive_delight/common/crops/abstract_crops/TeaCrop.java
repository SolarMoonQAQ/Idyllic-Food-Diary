package cn.solarmoon.immersive_delight.common.crops.abstract_crops;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TeaCrop extends SweetBerryBushBlock {

    /**
     * 默认属性
     */
    public TeaCrop() {
        super(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH));
    }

    /**
     * 自定属性
     */
    public TeaCrop(Properties properties) {
        super(properties);
    }

    /**
     * 仅仅造成减速
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity living) {
            living.makeStuckInBlock(state, new Vec3((double)0.8F, 0.75D, (double)0.8F));
        }
    }

}
