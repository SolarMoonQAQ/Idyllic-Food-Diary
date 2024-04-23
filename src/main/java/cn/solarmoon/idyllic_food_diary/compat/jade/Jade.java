package cn.solarmoon.idyllic_food_diary.compat.jade;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.common.block.base.entity_block.*;
import cn.solarmoon.idyllic_food_diary.compat.jade.provider.*;
import cn.solarmoon.solarmoon_core.api.common.block.crop.BaseBushCropBlock;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.impl.WailaClientRegistration;

@WailaPlugin
public class Jade implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new BushCropAgeProvider("bush_crop"), BaseBushCropBlock.class);
        registration.registerBlockComponent(new TimeRecipeProgressProvider("kettle"), AbstractKettleEntityBlock.class);
        registration.registerBlockComponent(new TimeRecipeProgressProvider("soup_pot"), AbstractSoupPotEntityBlock.class);
        registration.registerBlockComponent(new TimeRecipeProgressProvider("cup"), AbstractCupEntityBlock.class);
        registration.registerBlockComponent(new SteamerBaseProvider("steamer_base"), AbstractSteamerBaseEntityBlock.class);
        registration.registerBlockComponent(new IndividualTimeRecipeProgressProvider("steamer"), AbstractSteamerEntityBlock.class);
        registration.registerBlockComponent(new IndividualTimeRecipeProgressProvider("grill"), AbstractGrillEntityBlock.class);

        ResourceLocation pinId = new ResourceLocation(IdyllicFoodDiary.MOD_ID, "rolling_pin");
        WailaClientRegistration.INSTANCE.addConfig(pinId, true);
        registration.addTooltipCollectedCallback(OptionalRecipeItemProgressProvider.INSTANCE);
    }

}
