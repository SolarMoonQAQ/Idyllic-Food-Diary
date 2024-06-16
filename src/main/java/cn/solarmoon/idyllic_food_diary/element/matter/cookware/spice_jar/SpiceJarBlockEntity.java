package cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar;

import cn.solarmoon.idyllic_food_diary.feature.spice.SpiceJarInventory;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SpiceJarBlockEntity extends BlockEntity implements IContainerBE {

    private final ItemStackHandler inventory;

    public SpiceJarBlockEntity(int slotSize, BlockPos pos, BlockState state) {
        super(IMBlockEntities.SPICE_JAR.get(), pos, state);
        inventory = new SpiceJarInventory(slotSize);
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
