package cn.solarmoon.idyllic_food_diary.element.matter.cookware.container;

import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LongContainerBlockEntity extends ContainerBlockEntity {
    public LongContainerBlockEntity(int size, BlockPos pos, BlockState state) {
        super(IMBlockEntities.LONG_CONTAINER.get(), size, pos, state);
    }
}
