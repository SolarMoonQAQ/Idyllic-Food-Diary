package cn.solarmoon.idyllic_food_diary.element.matter.food;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpicesCap;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.solarmoon_core.api.block_base.BasicEntityBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 所有foodBlock的基本类，目的是给食物一个blockEntity的动态渲染
 */
public abstract class FoodEntityBlock extends BasicEntityBlock implements IHorizontalFacingBlock {

    public FoodEntityBlock(Properties properties) {
        super(properties);
    }

    public abstract VoxelShape getOriginShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    /**
     * 设置容器属性
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        if (fb == null) return;
        ItemStack container = ContainerHelper.getContainer(stack);
        fb.setContainer(container);
        fb.setSpices(stack);
        fb.dataNormalizeOpposite();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.FOOD.get();
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        var origin = super.getDrops(state, builder);
        FoodBlockEntity fb = (FoodBlockEntity) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (fb != null) {
            for (var item : origin) {
                ContainerHelper.setContainer(item, fb.getContainer());
                item.getCapability(IMCapabilities.FOOD_ITEM_DATA).ifPresent(d -> {
                    SpicesCap spicesData = d.getSpicesData();
                    spicesData.getSpices().addAll(fb.getSpices());
                });
            }
        }
        return origin;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        ItemStack origin = super.getCloneItemStack(level, pos, state);
        if (fb != null) {
            ContainerHelper.setContainer(origin, fb.getContainer());
            origin.getCapability(IMCapabilities.FOOD_ITEM_DATA).ifPresent(d -> {
                SpicesCap spicesData = d.getSpicesData();
                spicesData.getSpices().addAll(fb.getSpices());
            });
        }
        return origin;
    }

    /**
     * @return 使得声音类型为容器的声音类型
     */
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

    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        super.tick(level, pos, state, blockEntity);
        FoodBlockEntity fb = (FoodBlockEntity) blockEntity;
    }

    /**
     * @return 合并原始碰撞箱和容器碰撞箱
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        FoodBlockEntity fb = (FoodBlockEntity) level.getBlockEntity(pos);
        VoxelShape container = Shapes.empty();
        if (fb != null) {
            container = fb.getContainerLeft().getShape(level, pos, context);
        }
        return Shapes.or(getOriginShape(state, level, pos, context), container);
    }

}
