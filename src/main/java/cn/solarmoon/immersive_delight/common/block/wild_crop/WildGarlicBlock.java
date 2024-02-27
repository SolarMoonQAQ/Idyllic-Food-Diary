package cn.solarmoon.immersive_delight.common.block.wild_crop;

import cn.solarmoon.immersive_delight.common.block.base.crop.AbstractWildCropBlock;
import net.minecraft.world.effect.MobEffects;

public class WildGarlicBlock extends AbstractWildCropBlock {

    public WildGarlicBlock() {
        super(MobEffects.UNLUCK, 8);
    }

}
