package cn.solarmoon.idyllic_food_diary.compat.jade;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.CupBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlock;
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
        registration.registerBlockComponent(new TimeRecipeProgressProvider("kettle"), KettleBlock.class);
        registration.registerBlockComponent(new TimeRecipeProgressProvider("soup_pot"), CookingPotBlock.class);
        registration.registerBlockComponent(new TimeRecipeProgressProvider("cup"), CupBlock.class);
        registration.registerBlockComponent(new IndividualTimeRecipeProgressProvider("steamer"), SteamerBlock.class);
        registration.registerBlockComponent(new IndividualTimeRecipeProgressProvider("grill"), GrillBlock.class);

        ResourceLocation pinId = new ResourceLocation(IdyllicFoodDiary.MOD_ID, "rolling_pin");
        WailaClientRegistration.INSTANCE.addConfig(pinId, true);
        registration.addTooltipCollectedCallback(OptionalRecipeItemProgressProvider.INSTANCE);
    }

}
