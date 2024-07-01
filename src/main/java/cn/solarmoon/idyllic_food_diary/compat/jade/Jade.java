package cn.solarmoon.idyllic_food_diary.compat.jade;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cooking_pot.CookingPotBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cup.AbstractCupBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.cutting_board.CuttingBoardBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.grill.GrillBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.winnowing_basket.WinnowingBasketBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlock;
import cn.solarmoon.idyllic_food_diary.element.matter.stove.water_storage_stove.WaterStorageStoveBlock;
import cn.solarmoon.solarmoon_core.api.block_base.BaseBushCropBlock;
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
        registration.registerBlockComponent(new CommonCookwareProvider("wok"), WokBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("kettle"), KettleBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("cooking_pot"), CookingPotBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("cup"), AbstractCupBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("cutting_board"), CuttingBoardBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("steamer"), SteamerBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("grill"), GrillBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("winnowing_basket"), WinnowingBasketBlock.class);
        registration.registerBlockComponent(new CommonCookwareProvider("water_storage_stove"), WaterStorageStoveBlock.class);

        ResourceLocation pinId = new ResourceLocation(IdyllicFoodDiary.MOD_ID, "rolling_pin");
        WailaClientRegistration.INSTANCE.addConfig(pinId, true);
        registration.addTooltipCollectedCallback(OptionalRecipeItemProgressProvider.INSTANCE);
    }

}
