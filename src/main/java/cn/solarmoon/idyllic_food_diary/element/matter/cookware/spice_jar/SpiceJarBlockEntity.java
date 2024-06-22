package cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceJarInventory;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import cn.solarmoon.solarmoon_core.api.tile.IContainerTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SpiceJarBlockEntity extends BlockEntity implements IContainerTile {

    private final SpiceJarInventory inventory;

    public SpiceJarBlockEntity(int slotSize, BlockPos pos, BlockState state) {
        super(IMBlockEntities.SPICE_JAR.get(), pos, state);
        inventory = new SpiceJarInventory(1, slotSize, this);
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
