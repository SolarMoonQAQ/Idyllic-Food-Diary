package cn.solarmoon.immersive_delight.compat.jade;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractCupEntityBlock;
import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractKettleEntityBlock;
import cn.solarmoon.immersive_delight.common.block.base.entity_block.AbstractSoupPotEntityBlock;
import cn.solarmoon.immersive_delight.compat.jade.provider.OptionalRecipeItemProgressProvider;
import cn.solarmoon.immersive_delight.compat.jade.provider.TankClassRecipeProgressProvider;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.impl.WailaClientRegistration;

@WailaPlugin
public class Jade implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new TankClassRecipeProgressProvider("kettle"), AbstractKettleEntityBlock.class);
        registration.registerBlockComponent(new TankClassRecipeProgressProvider("soup_pot"), AbstractSoupPotEntityBlock.class);
        registration.registerBlockComponent(new TankClassRecipeProgressProvider("cup"), AbstractCupEntityBlock.class);

        ResourceLocation pinId = new ResourceLocation(ImmersiveDelight.MOD_ID, "rolling_pin");
        WailaClientRegistration.INSTANCE.addConfig(pinId, true);
        registration.addTooltipCollectedCallback(OptionalRecipeItemProgressProvider.INSTANCE);
    }

}
