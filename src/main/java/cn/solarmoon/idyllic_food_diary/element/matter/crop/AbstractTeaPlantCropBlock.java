package cn.solarmoon.idyllic_food_diary.element.matter.crop;

import cn.solarmoon.idyllic_food_diary.registry.common.IMEffects;
import cn.solarmoon.solarmoon_core.api.block_base.BaseBushCropBlock;
import cn.solarmoon.solarmoon_core.api.blockstate_access.IHorizontalFacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractTeaPlantCropBlock extends BaseBushCropBlock implements IHorizontalFacingBlock {

    @Override
    public int getMaxAge() {
        return 4;
    }

    /**
     * 接触后给予茶香效果
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (entity instanceof LivingEntity le) {
            le.addEffect(new MobEffectInstance(IMEffects.TEA_AROMA.get(), 200, 0, false, false, true));
        }
    }

}
