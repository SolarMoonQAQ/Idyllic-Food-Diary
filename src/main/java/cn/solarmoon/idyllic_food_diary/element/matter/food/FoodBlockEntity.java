package cn.solarmoon.idyllic_food_diary.element.matter.food;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceList;
import cn.solarmoon.idyllic_food_diary.feature.spice.SpicesCap;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.idyllic_food_diary.feature.basic_feature.ContainerHelper;
import cn.solarmoon.idyllic_food_diary.registry.common.IMCapabilities;
import cn.solarmoon.solarmoon_core.api.ability.CustomPlaceableItem;
import cn.solarmoon.solarmoon_core.api.block_util.BlockUtil;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.village.WandererTradesEvent;
import org.jetbrains.annotations.Nullable;

/**
 * 所有foodBlock的基本方块实体类，目的是给食物一个容器的动态渲染
 */
public class FoodBlockEntity extends SyncedBlockEntity {

    private ItemStack container;
    private final SpiceList spices;

    public FoodBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.FOOD.get(), pos, state);
        container = ItemStack.EMPTY;
        spices = new SpiceList();
    }

    @Nullable
    public FoodBlockEntity getFootState() {
        BlockPos pos = getBlockPos();
        BlockState state = getBlockState();
        Level level = getLevel();
        if (level == null) return null;
        if (state.getBlock() instanceof IBedPartBlock bedPartBlock) {
            BlockPos footPos = bedPartBlock.getFootPos(state, pos);
            if (!footPos.equals(pos)) { // 注意这一行间接使得如果是尾部则自动不进行同步，节省性能
                if (level.getBlockEntity(footPos) instanceof FoodBlockEntity fb) {
                    return fb;
                }
            }
        }
        return null;
    }

    /**
     * 在任何需要进行双方块同步的地方插入此方法，可以随时同步尾方块的数据到头部
     */
    public void dataNormalize() {
        FoodBlockEntity foot = getFootState();
        if (foot != null) {
            CompoundTag tag = foot.serializeNBT();
            deserializeNBT(tag);
        }
    }

    /**
     * 同上，但是同步对象为另一侧的方块
     */
    public void dataNormalizeOpposite() {
        BlockState state = getBlockState();
        BlockPos pos = getBlockPos();
        if (level == null) return;
        if (state.getBlock() instanceof IBedPartBlock b) {
            Direction findDirection = b.getNeighbourDirection(state.getValue(IBedPartBlock.PART), state.getValue(IHorizontalFacingBlock.FACING));
            BlockPos oppPos = pos.relative(findDirection);
            if (level.getBlockEntity(oppPos) instanceof FoodBlockEntity f) {
                f.dataNormalize();
            }
        }
    }

    public boolean hasContainer() {
        return !container.isEmpty();
    }

    public ItemStack getContainer() {
        return container;
    }

    public void setContainer(ItemStack container) {
        this.container = container;
        setChanged();
    }

    public SpiceList getSpices() {
        return spices;
    }

    public void setSpices(SpiceList spices) {
        this.spices.clear();
        this.spices.addAll(spices);
        setChanged();
    }

    public void setSpices(ItemStack stack) {
        stack.getCapability(IMCapabilities.FOOD_ITEM_DATA).ifPresent(d -> {
            SpicesCap spicesCap = d.getSpicesData();
            setSpices(spicesCap.getSpices());
        });
    }

    public BlockState getContainerLeft() {
        Block customPlace = CustomPlaceableItem.get().get(container.getItem());
        BlockState origin = customPlace != null ? customPlace.defaultBlockState() : Block.byItem(container.getItem()).defaultBlockState();
        origin = BlockUtil.inheritBlockWithAllState(getBlockState(), origin);
        return origin;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ContainerHelper.FOOD_CONTAINER, container.save(new CompoundTag()));
        tag.put(SpicesCap.SPICES, spices.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        container = ItemStack.of(tag.getCompound(ContainerHelper.FOOD_CONTAINER));
        spices.deserializeNBT(tag.getList(SpicesCap.SPICES, ListTag.TAG_COMPOUND));
    }

}
