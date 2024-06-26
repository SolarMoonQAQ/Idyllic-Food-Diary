package cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.little_cup;

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupBlockEntity;
import cn.solarmoon.idyllic_food_diary.registry.common.IMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LittleCupBlockEntity extends AbstractCupBlockEntity {

    public LittleCupBlockEntity(BlockPos pos, BlockState state) {
        super(1, 250, IMBlockEntities.LITTLE_CUP.get(), pos, state);
    }

}
