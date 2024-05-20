package cn.solarmoon.idyllic_food_diary.api.common.block.food_block;

import cn.solarmoon.idyllic_food_diary.api.util.ContainerHelper;
import cn.solarmoon.idyllic_food_diary.core.common.block_entity.FoodBlockEntity;
import cn.solarmoon.idyllic_food_diary.core.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.common.block.entity_block.BasicEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 所有foodBlock的基本类，目的是给食物一个blockEntity的动态渲染
 */
public abstract class FoodEntityBlock extends BasicEntityBlock {

    public FoodEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 设置容器属性
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        if (fb == null) return;
        fb.setContainer(ContainerHelper.getContainer(stack));
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.FOOD.get();
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        SoundType origin = super.getSoundType(state, level, pos, entity);
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        if (fb == null) return origin;
        if (fb.hasContainer()) {
            return fb.getContainerLeft().getSoundType();
        }
        return origin;
    }

}
