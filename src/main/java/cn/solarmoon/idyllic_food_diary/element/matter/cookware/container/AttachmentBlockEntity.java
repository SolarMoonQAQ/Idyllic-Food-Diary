package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IBedPartBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.tile.SyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttachmentBlockEntity extends BlockEntity {

    public AttachmentBlockEntity(BlockPos pos, BlockState state) {
        super(IMBlockEntities.ATTACHMENT.get(), pos, state);
    }

    public static Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    public static BlockPos getFootPos(BlockState state, BlockPos pos) {
        Direction nb = getNeighbourDirection(state.getValue(IBedPartBlock.PART), state.getValue(IHorizontalFacingBlock.FACING));
        return state.getValue(IBedPartBlock.PART) == BedPart.FOOT ? pos : pos.relative(nb);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        Level level = getLevel();
        BlockState state = getBlockState();
        BlockPos pos = getBlockPos();
        BlockPos footPos = getFootPos(state, pos);
        if (level != null) {
            BlockEntity be = level.getBlockEntity(footPos);
            if (be != null) {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return be.getCapability(ForgeCapabilities.ITEM_HANDLER)
                            .map(inv -> LazyOptional.of(() -> inv))
                            .orElse(LazyOptional.empty()).cast();
                }
            }
        }
        return super.getCapability(cap, side);
    }

}
